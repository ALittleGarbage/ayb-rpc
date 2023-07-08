package com.ayb.spring.boot.starter.registrar;

import com.ayb.core.rpc.client.RpcServiceProxy;
import com.ayb.spring.boot.starter.processor.RpcBeanPostProcessor;
import com.ayb.spring.boot.starter.processor.RpcClientInit;
import com.ayb.spring.boot.starter.processor.RpcServerInit;
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

        BeanDefinitionBuilder rpcClientInit = BeanDefinitionBuilder.genericBeanDefinition(RpcClientInit.class);
        registry.registerBeanDefinition("rpcClientInit", rpcClientInit.getBeanDefinition());

        BeanDefinitionBuilder rpcServerInit = BeanDefinitionBuilder.genericBeanDefinition(RpcServerInit.class);
        registry.registerBeanDefinition("rpcServerInit", rpcServerInit.getBeanDefinition());
    }
}
