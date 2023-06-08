package com.ayb.core.registry;


import com.ayb.common.annotation.SPI;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现接口
 *
 * @author ayb
 * @date 2023/6/3
 */
@SPI
public interface ServiceDiscovery {

    /**
     * 通过service名称，获取服务地址
     *
     * @param serviceName   service名称
     * @param serverAddress 注册中心地址
     * @return 服务地址
     */
    List<InetSocketAddress> lookupService(String serviceName, String serverAddress);
}
