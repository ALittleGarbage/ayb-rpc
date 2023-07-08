package com.ayb.core.loadbalance.impl;

import com.ayb.core.loadbalance.AbstractLoadBalance;
import com.ayb.core.rpc.protocol.RpcRequest;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 *
 * @author ayb
 * @date 2023/6/3
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected InetSocketAddress doSelect(List<InetSocketAddress> serviceList, RpcRequest rpcRequest) {
        Random random = new Random();
        return serviceList.get(random.nextInt(serviceList.size()));
    }
}
