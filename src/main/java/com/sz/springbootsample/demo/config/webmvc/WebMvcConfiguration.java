package com.sz.springbootsample.demo.config.webmvc;

import com.sz.springbootsample.demo.config.property.LogProperties;
import com.sz.springbootsample.demo.config.webmvc.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    LogProperties logProperties;

    @Value("${server.servlet.context-path:/}")
    private String applicationContextPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (logProperties.getEnable()) {
            LogInterceptor logInterceptor = new LogInterceptor(logProperties.getAdditionalSkipPattern());
            logInterceptor.setApplicationContextPath(applicationContextPath);
            registry.addInterceptor(logInterceptor);
        }
    }
}
