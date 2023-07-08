package com.ayb.spring.boot.starter.annotation;

import java.lang.annotation.*;

/**
 * 用于注入代理对象注解
 *
 * @author ayb
 * @date 2023/6/6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {
}
