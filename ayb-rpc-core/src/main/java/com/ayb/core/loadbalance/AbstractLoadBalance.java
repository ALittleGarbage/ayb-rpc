package com.ayb.core.loadbalance;


import com.ayb.core.rpc.protocol.RpcRequest;
import org.apache.commons.collections.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Abstract class for a load balancing policy
 *
 * @author shuang.kou
 * @createTime 2020年06月21日 07:44:00
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public InetSocketAddress getServiceAddress(List<InetSocketAddress> serviceList, RpcRequest rpcRequest) {
        if (CollectionUtils.isEmpty(serviceList)) {
            return null;
        }
        if (serviceList.size() == 1) {
            return serviceList.get(0);
        }
        return doSelect(serviceList, rpcRequest);
    }

    /**
     * 负载均衡的具体实现方法
     *
     * @param serviceList 服务地址列表
     * @param rpcRequest  请求参数
     * @return 服务地址
     */
    protected abstract InetSocketAddress doSelect(List<InetSocketAddress> serviceList, RpcRequest rpcRequest);

}
