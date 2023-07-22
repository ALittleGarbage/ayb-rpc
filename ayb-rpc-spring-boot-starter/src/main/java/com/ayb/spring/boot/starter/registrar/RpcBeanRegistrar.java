package com.ayb.spring.boot.starter.registrar;

import com.ayb.rpc.core.rpc.client.RpcServiceProxy;
import com.ayb.spring.boot.starter.processor.RpcBeanPostProcessor;
import com.ayb.spring.boot.starter.processor.RpcClientManager;
import com.ayb.spring.boot.starter.processor.RpcServerManager;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 将bean注册到spring容器
 *
 * @author ayb
 * @date 2023/6/6
 */
public class RpcBeanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder rpcPostProcessor = BeanDefinitionBuilder.genericBeanDefinition(RpcBeanPostProcessor.class);
        registry.registerBeanDefinition("rpcPostProcessor", rpcPostProcessor.getBeanDefinition());

        BeanDefinitionBuilder rpcServiceProxy = BeanDefinitionBuilder.genericBeanDefinition(RpcServiceProxy.class);
        registry.registerBeanDefinition("rpcServiceProxy", rpcServiceProxy.getBeanDefinition());

        BeanDefinitionBuilder rpcClientManager = BeanDefinitionBuilder.genericBeanDefinition(RpcClientManager.class);
        registry.registerBeanDefinition("rpcClientManager", rpcClientManager.getBeanDefinition());

        BeanDefinitionBuilder rpcServerManager = BeanDefinitionBuilder.genericBeanDefinition(RpcServerManager.class);
        registry.registerBeanDefinition("rpcServerManager", rpcServerManager.getBeanDefinition());
    }
}
