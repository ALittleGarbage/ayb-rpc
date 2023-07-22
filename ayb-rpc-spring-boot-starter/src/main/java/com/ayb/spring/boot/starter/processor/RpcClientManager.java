package com.ayb.spring.boot.starter.processor;

import com.ayb.rpc.core.rpc.Client;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 执行RpcClient初始化方法
 *
 * @author ayb
 * @date 2023/7/8
 */
public class RpcClientManager implements InitializingBean, DisposableBean {

    private final Client client;

    public RpcClientManager(Client client) {
        this.client = client;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client.start();
    }

    @Override
    public void destroy() throws Exception {
        client.close();
    }
}
