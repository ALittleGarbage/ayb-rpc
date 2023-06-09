package com.ayb.spring.boot.starter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rpcClient配置类
 *
 * @author ayb
 * @date 2023/6/5
 */
@Data
@ConfigurationProperties(prefix = "spring.ayb.rpc.client")
public class RpcClientProperties {

    /**
     * 连接超时时间（秒）
     */
    private Integer connectTimeoutSeconds = 5;
    /**
     * 写空闲发送心跳间隔（秒）
     */
    private Long heartbeatSeconds = 5L;
    /**
     * 请求超时时间
     */
    private int requestTimeOutSeconds = 3;
    /**
     * 默认负载均衡规则:Random
     */
    private String loadBalanceRule = "Random";
}
