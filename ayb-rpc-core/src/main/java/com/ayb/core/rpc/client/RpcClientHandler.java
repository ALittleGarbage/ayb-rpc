package com.ayb.core.rpc.client;

import com.ayb.common.enums.MessageTypeEnum;
import com.ayb.common.utils.IdWorkerUtils;
import com.ayb.core.config.RpcCodecConfig;
import com.ayb.core.rpc.protocol.RpcMessage;
import com.ayb.core.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * RpcClient响应处理
 *
 * @author ayb
 * @date 2023/6/4
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private final RpcCodecConfig rpcCodecConfig;

    public RpcClientHandler(RpcCodecConfig rpcCodecConfig) {
        this.rpcCodecConfig = rpcCodecConfig;
    }

    /**
     * 获取响应结果
     *
     * @param ctx
     * @param rpcMessage
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        if (rpcMessage.getMessageType() == MessageTypeEnum.RESPONSE.getCode()) {
            log.debug("收到rpc响应，地址:{}", ctx.channel().remoteAddress().toString());

            RpcResponseFuture.complete((RpcResponse) rpcMessage.getData());
        } else if (rpcMessage.getMessageType() == MessageTypeEnum.HEARTBEAT_RESPONSE.getCode()) {
            log.debug("收到心跳响应,地址:{}", ctx.channel().remoteAddress().toString());
        }
    }

    /**
     * 发送心跳
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            //特定时间没有写数据
            if (state == IdleState.WRITER_IDLE) {
                sendHeartBeat(ctx);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client端channel发生错误,原因:{}", cause.getMessage());
        ChannelProvider.remove((InetSocketAddress) ctx.channel().remoteAddress());
        ctx.close();
    }

    private void sendHeartBeat(ChannelHandlerContext ctx) {
        Long requestId = IdWorkerUtils.getInstance().nextId();

        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setRequestId(requestId);
        rpcMessage.setCompress(rpcCodecConfig.getCompressType().getCode());
        rpcMessage.setCodec(rpcCodecConfig.getSerializeType().getCode());
        rpcMessage.setMessageType(MessageTypeEnum.HEARTBEAT_REQUEST.getCode());
        rpcMessage.setData(MessageTypeEnum.HEARTBEAT_REQUEST.getName());

        ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }
}
