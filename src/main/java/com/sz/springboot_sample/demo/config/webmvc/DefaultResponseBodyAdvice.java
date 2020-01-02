package com.sz.springboot_sample.demo.config.webmvc;

import com.sz.springboot_sample.demo.thread.threadlocal.LogHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
@ControllerAdvice
public class DefaultResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
        response.setHeader("PointcutCount", LogHolder.getLogDto().getAdviceCount().toString());
        response.setHeader("LogStep", LogHolder.getLogDto().getLogStep().toString());
        return o;
    }
}
