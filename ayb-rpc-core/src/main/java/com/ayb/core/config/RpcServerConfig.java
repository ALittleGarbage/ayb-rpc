package com.ayb.core.config;

import com.ayb.common.enums.RegistryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc服务端配置类
 *
 * @author ayb
 * @date 2023/6/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcServerConfig {
    /**
     * 端口
     */
    private int port = 9999;
    /**
     * 读空闲超时时间（秒）
     */
    private long readerIdleTimeSeconds = 30;
    /**
     * 是否启用Nagle算法，默认开启
     */
    private boolean enableNagle = true;
    /**
     * 是否开启TCP底层心跳机制
     */
    private boolean enableKeepAlive = true;
    /**
     * 等待接受的连接队列的最大长度
     */
    private int maxQueueLength = 128;
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
