package com.ayb.example.server;

import com.ayb.core.config.RpcCodecConfig;
import com.ayb.core.config.RpcServerConfig;
import com.ayb.core.rpc.Server;
import com.ayb.core.rpc.server.RpcServer;
import com.ayb.example.impl.HelloServiceImpl;
import com.ayb.service.HelloService;

/**
 * serer测试
 *
 * @author ayb
 * @date 2023/6/5
 */
public class ServerTest {
    public static void main(String[] args) {
        //实例化helloService
        HelloService helloService = new HelloServiceImpl();
        //获取serviceName
        String serviceName = helloService.getClass().getInterfaces()[0].getName();

        //实例化server服务端
        RpcServerConfig rpcServerConfig = new RpcServerConfig();
        rpcServerConfig.setRegistryServerAddress("43.142.167.75:8848");
        rpcServerConfig.setRpcCodecConfig(new RpcCodecConfig());
        Server rpcServer = new RpcServer(rpcServerConfig);

        //注册HelloService
        rpcServer.registerService(serviceName, helloService);
        //启动
        rpcServer.start();
    }
}
