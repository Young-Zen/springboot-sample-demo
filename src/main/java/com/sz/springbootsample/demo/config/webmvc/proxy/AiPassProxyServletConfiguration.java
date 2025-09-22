package com.sz.springbootsample.demo.config.webmvc.proxy;

import javax.annotation.Resource;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yanghj
 * @date 2025/9/19 10:22
 */
@Configuration
public class AiPassProxyServletConfiguration {

    @Value("${custom.proxy.aiPass.servletUrl}")
    private String proxyServletUrl;

    @Value("${custom.proxy.aiPass.targetUrl}")
    private String targetUrl;

    @Resource private AiPassProxyServlet aiPassProxyServlet;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(aiPassProxyServlet, proxyServletUrl + "/*");
        servletRegistrationBean.addInitParameter(ProxyServlet.P_TARGET_URI, targetUrl);
        servletRegistrationBean.addInitParameter(ProxyServlet.P_LOG, "false");
        return servletRegistrationBean;
    }
}
