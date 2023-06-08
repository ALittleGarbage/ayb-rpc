package com.ayb.spring.boot.starter.annotation;

import com.ayb.spring.boot.starter.registrar.RpcBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ayb
 * @date 2023/6/6
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcBeanRegistrar.class})
@Documented
public @interface EnableAybRpc {

    String basePackage() default "";
}
