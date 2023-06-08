package com.ayb.core.registry.nacos.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ayb.common.exception.AybRpcException;
import com.ayb.core.registry.ServiceDiscovery;
import com.ayb.core.registry.nacos.NacosUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Naocs服务发现
 *
 * @author ayb
 * @date 2023/6/3
 */
@Slf4j
public class NacosServiceDiscoveryImpl implements ServiceDiscovery {

    @Override
    public List<InetSocketAddress> lookupService(String serviceName, String serverAddress) {

        try {
            List<Instance> allServer = NacosUtils.getNacosClient(serverAddress).getAllInstances(serviceName);

            return allServer.stream()
                    .map(instance -> InetSocketAddress.createUnresolved(instance.getIp(), instance.getPort()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            AybRpcException.cast(e.getMessage());
        }

        return null;
    }
}
