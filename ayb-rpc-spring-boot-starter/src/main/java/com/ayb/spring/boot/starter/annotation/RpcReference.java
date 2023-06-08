package com.ayb.spring.boot.starter.annotation;

import java.lang.annotation.*;

/**
 * @author ayb
 * @date 2023/6/6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {
}
