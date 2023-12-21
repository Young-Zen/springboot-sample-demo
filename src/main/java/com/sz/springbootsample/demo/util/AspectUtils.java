package com.sz.springbootsample.demo.util;

import java.lang.annotation.Annotation;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

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
        Object[] params = joinPoint.getArgs();
        Object[] arguments = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof ServletRequest
                    || params[i] instanceof ServletResponse
                    || params[i] instanceof MultipartFile) {
                // ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal
                // to call this method if the current request is not in asynchronous mode (i.e.
                // isAsyncStarted() returns false)
                // ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException:
                // getOutputStream() has already been called for this response
                continue;
            }
            arguments[i] = params[i];
        }
        return arguments;
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
