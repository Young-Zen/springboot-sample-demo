package com.sz.springboot_sample.demo.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.annotation.Annotation;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
public class AspectUtils {
    private static class AspectUtilsHolder {
        private static final AspectUtils INSTANCE = new AspectUtils();
    }

    private AspectUtils() {
    }

    public static final AspectUtils getInstance() {
        return AspectUtilsHolder.INSTANCE;
    }

    /**
     * 获取方法的参数
     *
     * @param joinPoint
     * @return
     */
    public Object[] getMethodParams(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        Object[] arguments = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof ServletRequest || params[i] instanceof ServletResponse || params[i] instanceof MultipartFile) {
                //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                continue;
            }
            arguments[i] = params[i];
        }
        return arguments;
    }

    public <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(annotationClass);
    }
}
