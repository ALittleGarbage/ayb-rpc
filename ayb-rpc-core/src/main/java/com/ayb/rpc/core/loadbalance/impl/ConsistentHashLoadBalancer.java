package com.ayb.rpc.core.loadbalance.impl;

import com.ayb.rpc.core.loadbalance.AbstractLoadBalance;
import com.ayb.rpc.core.rpc.protocol.RpcRequest;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性哈希负载均衡
 *
 * @author ayb
 * @date 2023/6/7
 */
public class ConsistentHashLoadBalancer extends AbstractLoadBalance {

    private final ConcurrentHashMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    @Override
    protected InetSocketAddress doSelect(List<InetSocketAddress> serviceList, RpcRequest rpcRequest) {
        int identityHashCode = System.identityHashCode(serviceList);

        String serviceName = rpcRequest.getServiceName();
        ConsistentHashSelector selector = selectors.get(serviceName);

        if (selector == null || selector.identityHashCode != identityHashCode) {
            selectors.put(serviceName, new ConsistentHashSelector(serviceList, 160, identityHashCode));
            selector = selectors.get(serviceName);
        }
        return selector.select(serviceName + Arrays.stream(rpcRequest.getParameters()));
    }

    static class ConsistentHashSelector {

        private static final int COUNT = 4;
        private final TreeMap<Long, InetSocketAddress> virtualInvokers;
        private final int identityHashCode;

        ConsistentHashSelector(List<InetSocketAddress> serviceList, int replicaNumber, int identityHashCode) {
            this.virtualInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;

            serviceList.forEach(service -> {
                for (int i = 0; i < replicaNumber / COUNT; i++) {
                    byte[] digest = md5(service.toString() + i);
                    for (int h = 0; h < COUNT; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, service);
                    }
                }
            });
        }

        static byte[] md5(String key) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

            return md.digest();
        }

        static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 255) << 24 | (long) (digest[2 + idx * 4] & 255) << 16 | (long) (digest[1 + idx * 4] & 255) << 8 | (long) (digest[idx * 4] & 255)) & 4294967295L;
        }

        public InetSocketAddress select(String rpcServiceKey) {
            byte[] digest = md5(rpcServiceKey);
            return selectForKey(hash(digest, 0));
        }

        public InetSocketAddress selectForKey(long hashCode) {
            Map.Entry<Long, InetSocketAddress> entry = virtualInvokers.tailMap(hashCode, true).firstEntry();

            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }

            return entry.getValue();
        }
    }
}
