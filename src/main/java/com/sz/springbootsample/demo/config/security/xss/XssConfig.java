package com.sz.springbootsample.demo.config.security.xss;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Yanghj
 * @date 2023/6/25 16:46
 */
@Configuration
public class XssConfig {
    @Bean
    public FilterRegistrationBean<Filter> xxsFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("XssFilter");
        registration.setOrder(1);
        return registration;
    }

    public static class XssFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String contentType = httpServletRequest.getContentType();
            if (Objects.equals(MediaType.MULTIPART_FORM_DATA_VALUE, contentType)) {
                // 如果是文件上传请求，跳过XSS过滤器
                chain.doFilter(request, response);
                return;
            }

            // 如果不是文件上传请求，进行XSS过滤
            chain.doFilter(new XssHttpServletRequestWrapper(httpServletRequest), response);
        }
    }
}
