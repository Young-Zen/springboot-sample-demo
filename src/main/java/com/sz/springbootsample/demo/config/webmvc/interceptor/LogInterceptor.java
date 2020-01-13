package com.sz.springbootsample.demo.config.webmvc.interceptor;

import com.sz.springbootsample.demo.annotation.IgnoreTracing;
import com.sz.springbootsample.demo.dto.LogDTO;
import com.sz.springbootsample.demo.thread.threadlocal.LogHolder;
import com.sz.springbootsample.demo.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String DEFAULT_SKIP_PATTERN = "/swagger.*";

    private final Pattern pattern;

    private String applicationContextPath = "/";

    public LogInterceptor(String additionalSkipPattern) {
        String skipPattern = StringUtils.hasText(additionalSkipPattern) ? DEFAULT_SKIP_PATTERN + "|" + additionalSkipPattern : DEFAULT_SKIP_PATTERN;
        pattern = Pattern.compile(skipPattern);
    }

    public LogInterceptor setApplicationContextPath(String applicationContextPath) {
        this.applicationContextPath = applicationContextPath;
        return this;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        LogDTO logDTO = new LogDTO();
        String logCode = "http_" + UUID.randomUUID();
        logDTO.setLogCode(logCode)
                .setLogStep(0)
                .setAdviceCount(0)
                .setIsThrowing(false)
                .setIsIgnoreTracing(false);
        // 设置 isIgnoreTracing
        IgnoreTracing ignoreTracing = ((HandlerMethod) handler).getMethod().getAnnotation(IgnoreTracing.class);
        if (ignoreTracing != null) {
            logDTO.setIsIgnoreTracing(true);
        }

        Matcher matcher = pattern.matcher(this.trimHead(request.getRequestURI(), "/".equals(applicationContextPath) ? "" : applicationContextPath));
        if (matcher.matches()) {
            logDTO.setIsIgnoreTracing(true);
            LogHolder.setLogDto(logDTO);
            return true;
        }

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

    private String trimHead(String source, String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return source;
        }
        if (source != null && source.startsWith(prefix)) {
            return source.substring(prefix.length());
        }
        return source;
    }
}
