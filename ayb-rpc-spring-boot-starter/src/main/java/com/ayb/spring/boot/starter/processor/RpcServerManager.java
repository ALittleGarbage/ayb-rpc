package com.ayb.spring.boot.starter.processor;

import com.ayb.rpc.core.rpc.Server;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 执行RpcServer初始化方法
 *
 * @author ayb
 * @date 2023/7/8
 */
public class RpcServerManager implements InitializingBean, DisposableBean {

    private final Server server;

    public RpcServerManager(Server server) {
        this.server = server;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        server.start();
    }

    @Override
    public void destroy() throws Exception {
        server.close();
    }
}
