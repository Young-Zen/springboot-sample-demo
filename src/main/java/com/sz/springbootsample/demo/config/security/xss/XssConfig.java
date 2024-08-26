package com.sz.springbootsample.demo.config.security.xss;

import java.io.IOException;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Yanghj
 * @date 2023/6/25 16:46
 */
@Configuration
public class XssConfig {

    @Resource private ObjectMapper objectMapper;

    @Bean
    public FilterRegistrationBean<Filter> xxsFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter(objectMapper));
        registration.addUrlPatterns("/*");
        registration.setName("XssFilter");
        registration.setOrder(1);
        return registration;
    }

    public static class XssFilter implements Filter {

        private ObjectMapper objectMapper;

        public XssFilter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String contentType = httpServletRequest.getContentType();
            if (Objects.equals(MediaType.MULTIPART_FORM_DATA_VALUE, contentType)) {
                // 如果是文件上传请求，跳过XSS过滤器
                chain.doFilter(request, response);
                return;
            }

            // 如果不是文件上传请求，进行XSS过滤
            chain.doFilter(
                    new XssHttpServletRequestWrapper(httpServletRequest, objectMapper), response);
        }
    }
}
