package com.sz.springbootsample.demo.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    private RequestUtils() {
    }

    public static final RequestUtils getInstance() {
        return RequestUtilsHolder.INSTANCE;
    }

    /**
     * 获取当前请求的 requestAttributes；
     * 线程安全。
     *
     * @return {@link ServletRequestAttributes}
     */
    public ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    /**
     * 获取当前请求对象；
     * 线程安全。
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
     * 获取当前请求的响应对象；
     * 线程安全。
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

    /**
     * 获取访问用户的客户端 IP
     *
     * @return IP地址
     */
    public String getRemoteIp() {
        return this.getRemoteIp(this.getHttpServletRequest());
    }

    /**
     * 获取访问用户的客户端 IP
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return IP地址
     */
    public String getRemoteIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
