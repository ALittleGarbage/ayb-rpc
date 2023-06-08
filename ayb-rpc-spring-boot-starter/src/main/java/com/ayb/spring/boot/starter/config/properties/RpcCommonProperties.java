package com.ayb.spring.boot.starter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rpc公共配置类
 *
 * @author ayb
 * @date 2023/6/5 0005
 */
@Data
@ConfigurationProperties(prefix = "spring.ayb.rpc")
public class RpcCommonProperties {

    /**
     * 默认注册中心类型:Nacos
     */
    public String registryType = "Nacos";
    /**
     * 魔数
     */
    private String magicNumber = "aybRpc";
    /**
     * 版本
     */
    private Byte version = 1;
    /**
     * 消息的最大长度
     */
    private Integer maxFrameLength = 1024 * 1024 * 8;
    /**
     * 默认压缩类型:Gzip
     */
    private String compressType = "Gzip";
    /**
     * 默认序列化类型:Kryo
     */
    private String serializeType = "Kryo";
    /**
     * 注册中心服务地址
     */
    private String registryServerAddress;
}
