package com.ayb.core.config;

import com.ayb.common.enums.CompressTypeEnum;
import com.ayb.common.enums.SerializeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc编码配置类
 *
 * @author ayb
 * @date 2023/6/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcCodecConfig {
    /**
     * 魔数
     */
    private byte[] magicNumber = {(byte) 'a', (byte) 'y', (byte) 'b'};
    /**
     * 版本
     */
    private byte version = 1;
    /**
     * 消息的最大长度
     */
    private int maxFrameLength = 1024 * 1024 * 8;
    /**
     * 默认压缩类型:Gzip
     */
    private CompressTypeEnum compressType = CompressTypeEnum.GZIP;
    /**
     * 默认序列化类型:Kryo
     */
    private SerializeTypeEnum serializeType = SerializeTypeEnum.KRYO;
}
