package com.ayb.core.loadbalance.impl;

import com.ayb.core.loadbalance.AbstractLoadBalance;
import com.ayb.core.rpc.protocol.RpcRequest;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡
 *
 * @author ayb
 * @date 2023/6/7
 */
public class RoundLoadBalancer extends AbstractLoadBalance {

    private final Map<String, AtomicInteger> counterMap = new ConcurrentHashMap<>();

    @Override
    protected InetSocketAddress doSelect(List<InetSocketAddress> serviceList, RpcRequest rpcRequest) {
        serviceList.sort(Comparator.comparing(InetSocketAddress::toString));

        String serverJoin = StringUtils.join(serviceList, ";");

        AtomicInteger counter = counterMap.get(serverJoin);
        if (counter == null) {
            synchronized (counterMap) {
                if ((counter = counterMap.get(serverJoin)) == null) {
                    counter = new AtomicInteger(0);
                    counterMap.put(serverJoin, counter);
                }
            }
        }

        return serviceList.get(incrementAndGetSize(serviceList.size(), counter));
    }

    private int incrementAndGetSize(int size, AtomicInteger counter) {
        for (; ; ) {
            int current = counter.get();
            int next = (current + 1) % size;
            if (counter.compareAndSet(current, next)) {
                return current;
            }
        }
    }
}
