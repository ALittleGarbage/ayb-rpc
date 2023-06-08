package com.ayb.spring.boot.starter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rpcServer配置类
 *
 * @author ayb
 * @date 2023/6/5
 */
@Data
@ConfigurationProperties(prefix = "spring.ayb.rpc.server")
public class RpcServerProperties {
    /**
     * 读空闲超时时间（秒）
     */
    private Long readerIdleTimeSeconds = 30L;
    /**
     * 是否启用Nagle算法，默认开启
     */
    private Boolean enableNagle = true;
    /**
     * 是否开启TCP底层心跳机制
     */
    private Boolean enableKeepAlive = true;
    /**
     * 等待接受的连接队列的最大长度
     */
    private Integer maxQueueLength = 128;
    /**
     * 端口
     */
    private Integer port = 9999;
}
