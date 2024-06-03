package com.sz.springbootsample.demo.config.webmvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sz.springbootsample.demo.config.property.LogProperties;
import com.sz.springbootsample.demo.config.webmvc.interceptor.LogInterceptor;
import com.sz.springbootsample.demo.config.webmvc.resolver.CustomParamResolver;
import com.sz.springbootsample.demo.config.webmvc.resolver.ParamResolver;

/**
 * Spring WebMvc配置类
 *
 * @author Yanghj
 * @date 1/2/2020
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired private LogProperties logProperties;

    @Autowired(required = false)
    private Map<String, ParamResolver> paramResolverMap = new HashMap<>(2);

    @Value("${server.servlet.context-path:/}")
    private String applicationContextPath;

    @PostConstruct
    void init() {
        paramResolverMap =
                paramResolverMap.values().stream()
                        .collect(
                                Collectors.toMap(
                                        ParamResolver::getParamName,
                                        Function.identity(),
                                        (k1, k2) -> k1));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        if (!CollectionUtils.isEmpty(paramResolverMap)) {
            resolvers.add(new CustomParamResolver(paramResolverMap));
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (logProperties.getEnable()) {
            LogInterceptor logInterceptor =
                    new LogInterceptor(logProperties.getAdditionalSkipPattern());
            logInterceptor.setApplicationContextPath(applicationContextPath);
            registry.addInterceptor(logInterceptor);
        }
    }
}
