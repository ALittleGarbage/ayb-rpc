package com.ayb.core.loadbalance.impl;

import com.ayb.core.loadbalance.LoadBalance;
import com.ayb.core.rpc.protocol.RpcRequest;
import org.apache.commons.collections.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 *
 * @author ayb
 * @date 2023/6/3
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public InetSocketAddress getServiceAddress(List<InetSocketAddress> serviceList, RpcRequest rpcRequest) {
        if (CollectionUtils.isEmpty(serviceList)) {
            return null;
        }

        Random random = new Random();

        return serviceList.get(random.nextInt(serviceList.size()));
    }
}
