package com.ayb.core.config;

import com.ayb.common.enums.LoadBalanceRuleEnum;
import com.ayb.common.enums.RegistryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc客户端配置类
 *
 * @author ayb
 * @date 2023/6/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcClientConfig {

    /**
     * 连接超时时间（秒）
     */
    private int connectTimeoutSeconds = 5;
    /**
     * 写空闲发送心跳间隔（秒）
     */
    private long heartbeatSeconds = 5L;
    /**
     * 请求超时时间
     */
    private int requestTimeOutSeconds = 3;
    /**
     * 默认负载均衡规则:Random
     */
    private LoadBalanceRuleEnum loadBalanceRule = LoadBalanceRuleEnum.RANDOM;
    /**
     * 默认注册中心类型:Nacos
     */
    private RegistryTypeEnum registryType = RegistryTypeEnum.NACOS;
    /**
     * 注册中心服务地址
     */
    private String registryServerAddress;
    /**
     * 编码配置
     */
    private RpcCodecConfig rpcCodecConfig;
}
