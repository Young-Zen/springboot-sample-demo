package com.sz.springbootsample.demo.config.webmvc.proxy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.SneakyThrows;

/**
 * @author Yanghj
 * @date 2025/9/19 10:58
 */
@Component
public class AiPassProxyServlet extends ProxyServlet {

    @Value("${server.servlet.context-path:/}")
    private String applicationContextPath;

    @Value("${custom.proxy.aiPass.servletUrl}")
    private String proxyServletUrl;

    @Value("${custom.proxy.aiPass.token}")
    private String token;

    private String baseUrl;

    @PostConstruct
    void initAiPassProxyServlet() {
        baseUrl =
                StringUtils.trimTrailingCharacter(applicationContextPath.trim(), '/')
                        + proxyServletUrl;
    }

    private static final String JS_SUFFIX = ".js";
    public static final String AIPASS_CHATBOT_PREFIX = "/chatbot";
    public static final String AIPASS_UEDC_PREFIX = "\"/uedc/";
    public static final String AIPASS_API_PREFIX = "`/api/v1/";
    public static final String TOKEN_HTTP_HEADER = "Token";

    @SneakyThrows
    @Override
    protected HttpClientBuilder getHttpClientBuilder() {
        // 创建一个SSLContext，信任所有证书
        SSLContextBuilder builder = SSLContextBuilder.create();
        builder.loadTrustMaterial(new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslSocketFactory =
                new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
        return super.getHttpClientBuilder().setSSLSocketFactory(sslSocketFactory);
    }

    @Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        String requestUri = servletRequest.getRequestURI().substring(baseUrl.length());
        if (requestUri.endsWith(JS_SUFFIX)) {
            super.service(servletRequest, servletResponse);
            return;
        }

        if (!Objects.equals(servletRequest.getHeader(TOKEN_HTTP_HEADER), token)) {
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        super.service(servletRequest, servletResponse);
    }

    @Override
    protected void copyResponseEntity(
            HttpResponse proxyResponse,
            HttpServletResponse servletResponse,
            HttpRequest proxyRequest,
            HttpServletRequest servletRequest)
            throws IOException {
        int statusCode = proxyResponse.getStatusLine().getStatusCode();
        if (statusCode < HttpServletResponse.SC_OK
                || statusCode >= HttpServletResponse.SC_MULTIPLE_CHOICES) {
            super.copyResponseEntity(proxyResponse, servletResponse, proxyRequest, servletRequest);
            return;
        }

        HttpEntity entity = proxyResponse.getEntity();
        if (entity == null) {
            super.copyResponseEntity(proxyResponse, servletResponse, proxyRequest, servletRequest);
            return;
        }

        String requestUri = servletRequest.getRequestURI().substring(baseUrl.length());
        if (requestUri.startsWith(AIPASS_CHATBOT_PREFIX)) {
            String response = EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
            if (response.contains(AIPASS_UEDC_PREFIX)) {
                response = response.replaceAll(AIPASS_UEDC_PREFIX, "\"" + baseUrl + "/uedc/");
            }

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            HttpEntity httpEntity =
                    EntityBuilder.create()
                            .setContentType(ContentType.TEXT_HTML)
                            .setContentEncoding(StandardCharsets.UTF_8.name())
                            .setBinary(responseBytes)
                            .build();
            proxyResponse.setEntity(httpEntity);

            servletResponse.setStatus(HttpServletResponse.SC_OK);
            servletResponse.setContentLength(responseBytes.length);
            httpEntity.writeTo(servletResponse.getOutputStream());
            return;
        }

        if (requestUri.endsWith(JS_SUFFIX)) {
            String response = EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
            if (response.contains(AIPASS_API_PREFIX)) {
                response = response.replaceAll(AIPASS_API_PREFIX, "`" + baseUrl + "/api/v1/");
            }

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            HttpEntity httpEntity =
                    EntityBuilder.create()
                            .setContentType(ContentType.create("application/javascript"))
                            .setContentEncoding(StandardCharsets.UTF_8.name())
                            .setBinary(responseBytes)
                            .build();
            proxyResponse.setEntity(httpEntity);

            servletResponse.setStatus(HttpServletResponse.SC_OK);
            servletResponse.setContentLength(responseBytes.length);
            httpEntity.writeTo(servletResponse.getOutputStream());
            return;
        }

        super.copyResponseEntity(proxyResponse, servletResponse, proxyRequest, servletRequest);
    }
}
