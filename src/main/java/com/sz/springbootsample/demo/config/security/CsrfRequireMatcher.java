package com.sz.springbootsample.demo.config.security;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Yanghj
 * @date 1/12/2020
 */
@Component
public class CsrfRequireMatcher implements RequestMatcher {
    private static final Pattern ALLOWED_METHODS = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private static final List<String> LOCALHOST_PATTERNS = ImmutableList.of("127.0.0.1", "0:0:0:0:0:0:0:1");

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String applicationContextPath;

    private List<String> swaggerUrls;

    @PostConstruct
    public void init() {
        String temp;
        if ("80".equals(port)) {
            temp = "";
        } else {
            temp = ":" + port;
        }
        final String swaggerUrl1 = "http://localhost" + temp + this.trim(applicationContextPath, '/') + "/swagger-ui.html";
        final String swaggerUrl2 = "http://127.0.0.1" + temp + this.trim(applicationContextPath, '/') + "/swagger-ui.html";
        swaggerUrls = ImmutableList.of(swaggerUrl1, swaggerUrl2);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        // CSRF disabled on GET, HEAD, TRACE, OPTIONS (i.e. enabled for POST, PUT, DELETE)
        if (ALLOWED_METHODS.matcher(request.getMethod()).matches()) {
            return false;
        }

        // CSRF not required on localhost when swagger-ui is referer
        final String remoteHost = request.getRemoteHost();
        final String referer = request.getHeader("Referer");
        if (remoteHost != null && referer != null
                && LOCALHOST_PATTERNS.contains(remoteHost)
                && swaggerUrls.contains(referer)) {
            return false;
        }
        // otherwise, CSRF is required
        return true;
    }

    private String trim(String str, char c) {
        int len = str.length();
        int st = 0;

        while ((st < len) && (str.charAt(st) <= ' ')) {
            st++;
        }
        while ((st < len) && (str.charAt(len - 1) <= ' ')) {
            len--;
        }

        while ((st < len) && (str.charAt(len - 1) == c)) {
            len--;
        }
        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
    }
}
