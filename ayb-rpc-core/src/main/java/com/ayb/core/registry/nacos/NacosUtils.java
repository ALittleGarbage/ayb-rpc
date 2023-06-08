package com.ayb.core.registry.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.ayb.common.exception.AybRpcException;
import lombok.extern.slf4j.Slf4j;

/**
 * Nacos客户端
 *
 * @author ayb
 * @date 2023/6/3
 */
@Slf4j
public class NacosUtils {

    private static volatile NamingService nacosClient;

    public static NamingService getNacosClient(String serverAddress) {
        if (nacosClient == null) {
            synchronized (NacosUtils.class) {
                if (nacosClient == null) {
                    try {
                        nacosClient = NacosFactory.createNamingService(serverAddress);
                    } catch (NacosException e) {
                        log.error("连接Nacos失败,原因:{}", e.getMessage());
                        AybRpcException.cast("初始化Nacos失败,原因:" + e.getMessage());
                    }
                }
            }
        }

        return nacosClient;
    }
}