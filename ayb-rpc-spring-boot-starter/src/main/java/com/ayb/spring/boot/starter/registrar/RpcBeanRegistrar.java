package com.ayb.spring.boot.starter.registrar;

import com.ayb.core.rpc.client.RpcServiceProxy;
import com.ayb.spring.boot.starter.processor.RpcBeanPostProcessor;
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

    private static final String BASE_PACKAGE = "basePackage";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder rpcPostProcessor = BeanDefinitionBuilder.genericBeanDefinition(RpcBeanPostProcessor.class);
        registry.registerBeanDefinition("rpcPostProcessor", rpcPostProcessor.getBeanDefinition());

        BeanDefinitionBuilder rpcServiceProxy = BeanDefinitionBuilder.genericBeanDefinition(RpcServiceProxy.class);
        registry.registerBeanDefinition("rpcServiceProxy", rpcServiceProxy.getBeanDefinition());

/*        BeanDefinitionBuilder rpcInit = BeanDefinitionBuilder.genericBeanDefinition(RpcInitApplicationListener.class);
        registry.registerBeanDefinition("rpcInitApplicationListener", rpcInit.getBeanDefinition());*/

/*        // 获取EnableAybRpc注解中的basePackage值
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(EnableAybRpc.class.getName()));

        String basePackage = null;
        if (annotationAttributes != null) {
            // 获取basePackage字符串数组
            basePackage = annotationAttributes.getString(BASE_PACKAGE);
        }
        // 长度为0或者为空
        if (StringUtils.isEmpty(basePackage)) {
            // 获取默认路径
            basePackage = ((StandardAnnotationMetadata)metadata).getIntrospectedClass().getPackage().getName();
        }*/
    }
}
