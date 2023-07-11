package com.ayb.example.client;

import com.ayb.rpc.core.config.RpcClientConfig;
import com.ayb.rpc.core.config.RpcCodecConfig;
import com.ayb.rpc.core.rpc.Client;
import com.ayb.rpc.core.rpc.client.RpcClient;
import com.ayb.rpc.core.rpc.client.RpcServiceProxy;
import com.ayb.service.HelloService;

/**
 * client测试
 *
 * @author ayb
 * @date 2023/6/5
 */
public class ClientTest {
    public static void main(String[] args) {

        RpcClientConfig rpcClientConfig = new RpcClientConfig();
        rpcClientConfig.setRegistryServerAddress("43.142.167.75:8848");
        rpcClientConfig.setRpcCodecConfig(new RpcCodecConfig());

        //获取client客服端
        Client rpcClient = new RpcClient(rpcClientConfig);
        rpcClient.start();

        //生成代理
        RpcServiceProxy rpcServiceProxy = new RpcServiceProxy(rpcClient);
        HelloService helloService = rpcServiceProxy.getProxy(HelloService.class);

        //rpc远程调用方法
        String sayHello = helloService.sayHello("世界");

        System.out.println("sayHello = " + sayHello);
    }
}
