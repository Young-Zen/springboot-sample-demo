package com.sz.springboot_sample.demo.config.security;

import com.alibaba.fastjson.JSONObject;
import com.sz.springboot_sample.demo.dto.ResponseResultDTO;
import com.sz.springboot_sample.demo.enums.ResponseCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().write(JSONObject.toJSONString(ResponseResultDTO.fail(ResponseCodeEnum.FORBIDDEN)));
    }
}
