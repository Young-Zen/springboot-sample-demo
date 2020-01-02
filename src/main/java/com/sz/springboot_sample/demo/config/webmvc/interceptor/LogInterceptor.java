package com.sz.springboot_sample.demo.config.webmvc.interceptor;

import com.sz.springboot_sample.demo.dto.LogDTO;
import com.sz.springboot_sample.demo.thread.threadlocal.LogHolder;
import com.sz.springboot_sample.demo.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.UUID;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LogDTO logDTO = new LogDTO();
        String logCode = "http_" + UUID.randomUUID();
        logDTO.setLogCode(logCode)
                .setLogStep(0)
                .setAdviceCount(0)
                .setIsThrowing(false)
                .setIsIgnoreTracing(false);
        LogHolder.setLogDto(logDTO);
        log.info("{}，服务器IP：{}，请求IP：{}，请求方式：{}，URL：{}", logCode, InetAddress.getLocalHost().getHostAddress(), RequestUtils.getInstance().getRemoteIp(request), request.getMethod(), request.getRequestURL());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LogHolder.clean();
    }
}
