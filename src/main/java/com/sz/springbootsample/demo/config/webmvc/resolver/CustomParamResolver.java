package com.sz.springbootsample.demo.config.webmvc.resolver;

import java.util.Map;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.sz.springbootsample.demo.annotation.CustomParam;

/**
 * @author Yanghj
 * @date 2024/5/29 16:11
 */
public class CustomParamResolver implements HandlerMethodArgumentResolver {

    private Map<String, ParamResolver> paramResolverMap;

    public CustomParamResolver(Map<String, ParamResolver> paramResolverMap) {
        this.paramResolverMap = paramResolverMap;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CustomParam.class) != null;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {
        CustomParam customParamAnnotation = parameter.getParameterAnnotation(CustomParam.class);
        String paramName = customParamAnnotation.value();

        ParamResolver paramResolver = this.paramResolverMap.get(paramName);
        return Optional.ofNullable(paramResolver)
                .map(r -> r.getParamValue(parameter, mavContainer, webRequest, binderFactory))
                .orElse(null);
    }
}
