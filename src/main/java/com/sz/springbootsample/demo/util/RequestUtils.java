package com.sz.springbootsample.demo.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求工具类
 *
 * @author Yanghj
 * @date 1/2/2020
 */
public class RequestUtils {

    private static class RequestUtilsHolder {
        private static final RequestUtils INSTANCE = new RequestUtils();
    }

    private RequestUtils() {}

    public static final RequestUtils getInstance() {
        return RequestUtilsHolder.INSTANCE;
    }

    /**
     * 获取当前请求的 requestAttributes； 线程安全。
     *
     * @return {@link ServletRequestAttributes}
     */
    public ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取当前请求对象； 线程安全。
     *
     * @return 请求对象
     */
    public HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = this.getServletRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 获取当前请求的响应对象； 线程安全。
     *
     * @return 响应对象
     */
    public HttpServletResponse getHttpServletResponse() {
        RequestAttributes requestAttributes = this.getServletRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }
}
