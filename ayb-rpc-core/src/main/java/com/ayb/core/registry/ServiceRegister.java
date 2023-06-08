package com.ayb.core.registry;

import com.ayb.common.annotation.SPI;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 *
 * @author ayb
 * @date 2023/6/3
 */
@SPI
public interface ServiceRegister {

    /**
     * 获取service实例
     *
     * @param rpcServiceName service名称
     * @return service
     */
    Object getService(String rpcServiceName);

    /**
     * 注册service
     *
     * @param serviceName       service名称
     * @param service           service实例
     * @param inetSocketAddress 服务地址
     * @param serverAddress     注册中心地址
     */
    void registerService(String serviceName,
                         Object service,
                         InetSocketAddress inetSocketAddress,
                         String serverAddress);

    /**
     * 清除所有注册的service
     *
     * @param inetSocketAddress 服务地址
     * @param serverAddress     注册中心地址
     */
    void clearAllService(InetSocketAddress inetSocketAddress, String serverAddress);
}
