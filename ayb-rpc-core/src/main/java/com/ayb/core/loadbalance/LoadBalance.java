package com.ayb.core.loadbalance;

import com.ayb.common.annotation.SPI;
import com.ayb.core.rpc.protocol.RpcRequest;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 负载均衡规则接口
 *
 * @author ayb
 * @date 2023/6/1
 */
@SPI
public interface LoadBalance {
    /**
     * 根据负载均衡规则，选出一个服务地址
     *
     * @param serviceList 服务地址列表
     * @param rpcRequest  请求参数
     * @return 服务地址
     */
    InetSocketAddress getServiceAddress(List<InetSocketAddress> serviceList, RpcRequest rpcRequest);

}
