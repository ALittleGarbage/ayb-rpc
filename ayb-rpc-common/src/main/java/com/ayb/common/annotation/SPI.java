package com.ayb.common.annotation;

import java.lang.annotation.*;

/**
 * 用于标注一个接口
 *
 * @author ayb
 * @date 2023/6/5 0005
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

}
