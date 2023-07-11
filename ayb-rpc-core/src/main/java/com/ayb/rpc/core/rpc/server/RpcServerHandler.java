package com.ayb.rpc.core.rpc.server;

import com.ayb.rpc.common.enums.MessageTypeEnum;
import com.ayb.rpc.core.config.RpcCodecConfig;
import com.ayb.rpc.core.registry.ServiceRegister;
import com.ayb.rpc.core.rpc.protocol.RpcMessage;
import com.ayb.rpc.core.rpc.protocol.RpcRequest;
import com.ayb.rpc.core.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 处理rpc请求
 *
 * @author ayb
 * @date 2023/6/3
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private final RpcCodecConfig rpcCodecConfig;

    private ServiceRegister serviceRegister;


    public RpcServerHandler(RpcCodecConfig rpcCodecConfig, ServiceRegister serviceRegister) {
        this.rpcCodecConfig = rpcCodecConfig;
        this.serviceRegister = serviceRegister;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        Byte messageType = rpcMessage.getMessageType();

        rpcMessage.setCodec(rpcCodecConfig.getSerializeType().getCode());
        rpcMessage.setCompress(rpcCodecConfig.getCompressType().getCode());

        if (messageType == MessageTypeEnum.HEARTBEAT_REQUEST.getCode()) {
            log.debug("收到心跳请求,地址:{}", ctx.channel().remoteAddress().toString());

            rpcMessage.setMessageType(MessageTypeEnum.HEARTBEAT_RESPONSE.getCode());
            rpcMessage.setData(MessageTypeEnum.HEARTBEAT_RESPONSE.getName());
        } else {
            RpcRequest rpcRequest = (RpcRequest) rpcMessage.getData();
            log.debug("收到rpc请求,地址:{}", ctx.channel().remoteAddress().toString());

            Object service = serviceRegister.getService(rpcRequest.getServiceName());
            RpcResponse rpcResponse = handle(rpcRequest, service, rpcRequest.getRequestId());

            rpcMessage.setMessageType(MessageTypeEnum.RESPONSE.getCode());
            rpcMessage.setData(rpcResponse);
        }

        ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    /**
     * 空间状态处理
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            //特定时间没有读数据
            if (state == IdleState.READER_IDLE) {
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server端发生错误,原因:{}", cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * rpcRequest的参数调用接口的方法，获取结果
     *
     * @param rpcRequest
     * @param service
     * @param requestId
     * @return
     */
    private RpcResponse handle(RpcRequest rpcRequest, Object service, Long requestId) {
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object result = method.invoke(service, rpcRequest.getParameters());
            return RpcResponse.oK(requestId, result);
        } catch (Exception e) {
            log.error("调用{}服务中的{}方法失败,原因:{}", rpcRequest.getServiceName(), rpcRequest.getMethodName(), e.getMessage());
            return RpcResponse.fail(requestId, e.getMessage());
        }

    }
}
