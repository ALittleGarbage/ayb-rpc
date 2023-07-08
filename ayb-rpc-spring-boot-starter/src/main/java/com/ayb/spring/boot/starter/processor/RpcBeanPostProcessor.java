package com.ayb.spring.boot.starter.processor;

import com.ayb.common.exception.AybRpcException;
import com.ayb.core.rpc.Server;
import com.ayb.core.rpc.client.RpcServiceProxy;
import com.ayb.spring.boot.starter.annotation.RpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * 扫描注解，用于服务注册、注入代理对象
 *
 * @author ayb
 * @date 2023/6/7
 */
public class RpcBeanPostProcessor implements BeanPostProcessor {

    private final Server server;

    private final RpcServiceProxy rpcServiceProxy;

    public RpcBeanPostProcessor(Server server, RpcServiceProxy rpcServiceProxy) {
        this.server = server;
        this.rpcServiceProxy = rpcServiceProxy;
    }

    /**
     * service实例注册
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Service.class)) {
            String serviceName = bean.getClass().getInterfaces()[0].getName();
            System.out.println();
            server.registerService(serviceName, bean);
        }
        return bean;
    }

    /**
     * 将service接口的代理对象注入
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(RpcReference.class)) {
                Object clientProxy = rpcServiceProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    AybRpcException.cast(e.getMessage());
                }
            }
        }
        return bean;
    }
}
