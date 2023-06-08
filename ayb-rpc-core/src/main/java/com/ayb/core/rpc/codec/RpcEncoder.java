package com.ayb.core.rpc.codec;

import com.ayb.common.enums.CompressTypeEnum;
import com.ayb.common.enums.SerializeTypeEnum;
import com.ayb.common.exception.AybRpcException;
import com.ayb.common.extension.ExtensionLoader;
import com.ayb.core.compress.Compress;
import com.ayb.core.config.RpcCodecConfig;
import com.ayb.core.rpc.protocol.RpcMessage;
import com.ayb.core.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 编码器
 *
 * @author ayb
 * @date 2023/6/2
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder<RpcMessage> {

    private final RpcCodecConfig rpcCodecConfig;

    public RpcEncoder(RpcCodecConfig rpcCodecConfig) {
        this.rpcCodecConfig = rpcCodecConfig;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf byteBuf) {
        /*
          4B magic code（魔法数）     1B version（版本）     8B requestId（请求的Id）
          1B messageType（消息类型）  1B compress（压缩类型） 1B codec（序列化类型）
          4B body length（消息长度）
          body（object类型数据）
          */
        try {
            // 魔数
            byteBuf.writeBytes(rpcCodecConfig.getMagicNumber());
            // 版本
            byteBuf.writeByte(rpcCodecConfig.getVersion());
            // 请求id
            byteBuf.writeLong(rpcMessage.getRequestId());
            // 消息类型
            byteBuf.writeByte(rpcMessage.getMessageType());
            // 压缩类型
            byteBuf.writeByte(rpcMessage.getCompress());
            // 序列化类型
            byteBuf.writeByte(rpcMessage.getCodec());

            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                    .getExtension(SerializeTypeEnum.getName(rpcMessage.getCodec()));
            Compress compress = ExtensionLoader.getExtensionLoader(Compress.class)
                    .getExtension(CompressTypeEnum.getName(rpcMessage.getCompress()));

            byte[] serializeBytes = serializer.serialize(rpcMessage.getData());
            byte[] compressBytes = compress.compress(serializeBytes);

            // 消息长度
            byteBuf.writeInt(compressBytes.length);
            // 数据
            byteBuf.writeBytes(compressBytes);
        } catch (IOException e) {
            log.error("编码时发生错误,原因:{}", e.getMessage());
            AybRpcException.cast(e.getMessage());
        }
    }
}
