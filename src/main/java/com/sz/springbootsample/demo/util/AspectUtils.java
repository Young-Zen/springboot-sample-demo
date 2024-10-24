package com.sz.springbootsample.demo.util;

import java.lang.annotation.Annotation;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.sz.springbootsample.demo.annotation.IgnoreTracing;

/**
 * AOP工具类
 *
 * @author Yanghj
 * @date 1/2/2020
 */
public class AspectUtils {
    private static class AspectUtilsHolder {
        private static final AspectUtils INSTANCE = new AspectUtils();
    }

    private AspectUtils() {}

    public static final AspectUtils getInstance() {
        return AspectUtilsHolder.INSTANCE;
    }

    /**
     * 获取方法的可序列化参数
     *
     * @param joinPoint 连接点{@link JoinPoint}
     * @return Object[]
     */
    public Object[] getMethodParams(JoinPoint joinPoint) {
        if (joinPoint == null) {
            return null;
        }

        Annotation[][] parameterAnnotations =
                ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations();
        Object[] params = joinPoint.getArgs();
        int length = Math.min(params.length, parameterAnnotations.length);
        Object[] arguments = new Object[length];
        for (int i = 0; i < length; i++) {
            if (isIgnoreParamLog(parameterAnnotations[i])) {
                arguments[i] = "***";
                continue;
            }

            arguments[i] = params[i];
        }
        return arguments;
    }

    private boolean isIgnoreParamLog(Annotation[] parameterAnnotation) {
        if (parameterAnnotation == null || parameterAnnotation.length == 0) {
            return false;
        }

        for (Annotation annotation : parameterAnnotation) {
            if (Objects.equals(annotation.annotationType(), IgnoreTracing.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取方法上的注解
     *
     * @param joinPoint 连接点{@link JoinPoint}
     * @param annotationClass 注解的类型
     * @return 方法上的注解
     */
    public <T extends Annotation> T getMethodAnnotation(
            JoinPoint joinPoint, Class<T> annotationClass) {
        if (joinPoint == null) {
            return null;
        }
        return ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(annotationClass);
    }

    /**
     * 获取类上的注解
     *
     * @param joinPoint 连接点{@link JoinPoint}
     * @param annotationClass 注解的类型
     * @return 类上的注解
     */
    public <T extends Annotation> T getClassAnnotation(
            JoinPoint joinPoint, Class<T> annotationClass) {
        if (joinPoint == null) {
            return null;
        }
        return ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getDeclaringClass()
                .getAnnotation(annotationClass);
    }
}
