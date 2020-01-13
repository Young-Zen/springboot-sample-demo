package com.sz.springbootsample.demo.config.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Yanghj
 * @date 1/12/2020
 */
public class CsrfRequireMatcher implements RequestMatcher {
    private static final Pattern ALLOWED_METHODS = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

    private final List<String> allowedRemoteHost;

    private final List<String> allowedRefererList;

    public CsrfRequireMatcher(List<String> allowedRemoteHost, List<String> allowedRefererList) {
        this.allowedRemoteHost = allowedRemoteHost;
        this.allowedRefererList = allowedRefererList;
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
                && allowedRemoteHost.contains(remoteHost)
                && allowedRefererList.contains(referer)) {
            return false;
        }
        // otherwise, CSRF is required
        return true;
    }
}
