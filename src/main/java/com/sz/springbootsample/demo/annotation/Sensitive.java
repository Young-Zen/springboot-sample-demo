package com.sz.springbootsample.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.sz.springbootsample.demo.enums.DesensitizationStrategyEnum;
import com.sz.springbootsample.demo.serializer.SensitiveJsonSerializer;

/**
 * 接口数据脱敏注解
 *
 * @author Yanghj
 * @date 2024/10/24 19:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Sensitive {

    /** 脱敏策略 */
    DesensitizationStrategyEnum strategy() default DesensitizationStrategyEnum.DEFAULT;
}
