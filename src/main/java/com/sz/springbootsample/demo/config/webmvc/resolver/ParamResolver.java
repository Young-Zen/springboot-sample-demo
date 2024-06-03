package com.sz.springbootsample.demo.config.webmvc.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Yanghj
 * @date 2024/5/29 16:13
 */
public interface ParamResolver {

    /**
     * 返回参数名
     *
     * @return
     */
    String getParamName();

    /**
     * 解析参数值
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     */
    Object getParamValue(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory);
}
