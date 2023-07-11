package com.ayb.rpc.core.rpc.codec;

import com.ayb.rpc.common.enums.CompressTypeEnum;
import com.ayb.rpc.common.enums.MessageTypeEnum;
import com.ayb.rpc.common.enums.SerializeTypeEnum;
import com.ayb.rpc.common.extension.ExtensionLoader;
import com.ayb.rpc.core.compress.Compress;
import com.ayb.rpc.core.config.RpcCodecConfig;
import com.ayb.rpc.core.rpc.protocol.RpcMessage;
import com.ayb.rpc.core.rpc.protocol.RpcRequest;
import com.ayb.rpc.core.rpc.protocol.RpcResponse;
import com.ayb.rpc.core.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;


/**
 * 解码器
 *
 * @author ayb
 * @date 2023/6/2
 */
@Slf4j
public class RpcDecoder extends LengthFieldBasedFrameDecoder {

    private RpcCodecConfig rpcCodecConfig;

    public RpcDecoder(RpcCodecConfig rpcCodecConfig) {
        this(rpcCodecConfig.getMaxFrameLength(),
                12 + rpcCodecConfig.getMagicNumber().length,
                4,
                0,
                0);
        this.rpcCodecConfig = rpcCodecConfig;
    }

    public RpcDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        /**
         * 4B magic code（魔法数）     1B version（版本）     8B requestId（请求的Id）
         * 1B messageType（消息类型）  1B compress（压缩类型） 1B codec（序列化类型）
         * 4B body length（消息长度）
         * body（object类型数据）
         * */
        checkMagicNumber(frame);
        checkVersion(frame);
        long requestId = frame.readLong();
        byte messageType = frame.readByte();
        byte compressType = frame.readByte();
        byte codecType = frame.readByte();
        int bodyLength = frame.readInt();
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMessageType(messageType);
        rpcMessage.setRequestId(requestId);
        rpcMessage.setCodec(codecType);
        rpcMessage.setCompress(compressType);

        MessageTypeEnum message = MessageTypeEnum.getMessageType(messageType);

        if (message == MessageTypeEnum.HEARTBEAT_REQUEST) {
            rpcMessage.setData(MessageTypeEnum.HEARTBEAT_REQUEST.getName());
        } else if (message == MessageTypeEnum.HEARTBEAT_RESPONSE) {
            rpcMessage.setData(MessageTypeEnum.HEARTBEAT_RESPONSE.getName());
        } else {
            if (bodyLength > 0) {
                byte[] bytes = new byte[bodyLength];
                frame.readBytes(bytes);

                Compress compress = ExtensionLoader.getExtensionLoader(Compress.class)
                        .getExtension(CompressTypeEnum.getName(rpcMessage.getCompress()));
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                        .getExtension(SerializeTypeEnum.getName(rpcMessage.getCodec()));

                bytes = compress.decompress(bytes);

                if (message == MessageTypeEnum.REQUEST) {
                    RpcRequest request = serializer.deserialize(bytes, RpcRequest.class);
                    rpcMessage.setData(request);
                } else {
                    RpcResponse response = serializer.deserialize(bytes, RpcResponse.class);
                    rpcMessage.setData(response);
                }
            }
        }
        // 释放ByteBuf，防止内存泄漏
        frame.release();

        return rpcMessage;
    }

    private void checkMagicNumber(ByteBuf in) {
        byte[] magicNumber = rpcCodecConfig.getMagicNumber();
        // 获取长度
        int len = magicNumber.length;
        // 设置一个桶
        byte[] tmp = new byte[len];
        // 盛水
        in.readBytes(tmp);
        // 比较
        for (int i = 0; i < len; i++) {
            if (tmp[i] != magicNumber[i]) {
                log.error("魔法数错误:{}", Arrays.toString(tmp));
                throw new IllegalArgumentException("魔法数错误: " + Arrays.toString(tmp));
            }
        }
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != rpcCodecConfig.getVersion()) {
            log.error("魔法数错误:{}", version);
            throw new RuntimeException("版本不兼容:" + version);
        }
    }

}
