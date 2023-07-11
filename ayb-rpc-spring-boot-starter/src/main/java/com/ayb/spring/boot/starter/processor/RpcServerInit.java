package com.ayb.spring.boot.starter.processor;

import com.ayb.rpc.core.rpc.Server;
import org.springframework.beans.factory.InitializingBean;

/**
 * 执行RpcServer初始化方法
 *
 * @author ayb
 * @date 2023/7/8
 */
public class RpcServerInit implements InitializingBean {

    private final Server server;

    public RpcServerInit(Server server) {
        this.server = server;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        server.start();
    }
}
