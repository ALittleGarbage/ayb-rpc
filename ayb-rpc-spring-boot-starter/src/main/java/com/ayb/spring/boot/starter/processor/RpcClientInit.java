package com.ayb.spring.boot.starter.processor;

import com.ayb.core.rpc.Client;
import org.springframework.beans.factory.InitializingBean;

/**
 * 执行RpcClient初始化方法
 *
 * @author ayb
 * @date 2023/7/8
 */
public class RpcClientInit implements InitializingBean {

    private final Client client;

    public RpcClientInit(Client client) {
        this.client = client;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client.start();
    }
}
