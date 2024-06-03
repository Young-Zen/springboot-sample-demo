package com.sz.springbootsample.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yanghj
 * @date 2024/5/29 16:09
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomParam {

    /**
     * 请求参数
     *
     * @return
     */
    String value();
}
