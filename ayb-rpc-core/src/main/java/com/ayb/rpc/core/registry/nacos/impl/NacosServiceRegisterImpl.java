package com.ayb.rpc.core.registry.nacos.impl;

import com.alibaba.nacos.api.naming.NamingService;
import com.ayb.rpc.common.exception.AybRpcException;
import com.ayb.rpc.core.registry.ServiceRegister;
import com.ayb.rpc.core.registry.nacos.NacosUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Naocs服务注册
 *
 * @author ayb
 * @date 2023/6/3
 */
@Slf4j
public class NacosServiceRegisterImpl implements ServiceRegister {

    private static final Map<String, Object> RPC_SERVICES_MAP = new ConcurrentHashMap<>();

    @Override
    public Object getService(String rpcServiceName) {
        Object service = RPC_SERVICES_MAP.get(rpcServiceName);
        if (service == null) {
            throw new RuntimeException("获取到的service地址不存在");
        }

        return service;
    }

    @Override
    public void registerService(String serviceName,
                                Object service,
                                InetSocketAddress inetSocketAddress,
                                String serverAddress) {
        if (RPC_SERVICES_MAP.containsKey(serviceName)) {
            return;
        }

        try {
            NamingService nacosClient = NacosUtils.getNacosClient(serverAddress);
            nacosClient.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (Exception e) {
            log.error("服务注册到Nacos时出现错误:{}", e.getMessage());
            AybRpcException.cast("服务注册到Nacos错误,原因:" + e.getMessage());
        }

        RPC_SERVICES_MAP.put(serviceName, service);
    }

    @Override
    public void clearAllService(InetSocketAddress inetSocketAddress, String serverAddress) {
        for (String serviceName : RPC_SERVICES_MAP.keySet()) {
            try {
                NamingService nacosClient = NacosUtils.getNacosClient(serverAddress);
                nacosClient.deregisterInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
            } catch (Exception e) {
                log.error("删除注册信息失败,原因:{}", e.getMessage());
            }
        }
    }
}
