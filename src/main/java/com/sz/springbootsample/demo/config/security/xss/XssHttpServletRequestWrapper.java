package com.sz.springbootsample.demo.config.security.xss;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.CollectionUtils;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.util.IOUtils;
import io.micrometer.core.instrument.util.StringUtils;

/**
 * @author Yanghj
 * @date 2023/6/25 16:43
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private ObjectMapper objectMapper;

    public XssHttpServletRequestWrapper(HttpServletRequest request, ObjectMapper objectMapper) {
        super(request);
        this.objectMapper = objectMapper;
    }

    @Override
    public String getQueryString() {
        String value = super.getQueryString();
        if (StringUtils.isNotBlank(value)) {
            value = HtmlUtils.htmlEscape(value, StandardCharsets.UTF_8.name());
        }
        return value;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            value = HtmlUtils.htmlEscape(value, StandardCharsets.UTF_8.name());
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (Objects.nonNull(values)) {
            for (int i = 0; i < values.length; i++) {
                if (StringUtils.isNotBlank(values[i])) {
                    values[i] = HtmlUtils.htmlEscape(values[i], StandardCharsets.UTF_8.name());
                }
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        if (!CollectionUtils.isEmpty(map)) {
            for (String[] values : map.values()) {
                for (int i = 0; i < values.length; i++) {
                    if (StringUtils.isNotBlank(values[i])) {
                        values[i] = HtmlUtils.htmlEscape(values[i], StandardCharsets.UTF_8.name());
                    }
                }
            }
        }
        return map;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 读取 RequestBody 中的数据
        String requestBody = IOUtils.toString(super.getInputStream(), StandardCharsets.UTF_8);
        // 进行 XSS 转义
        String escapedRequestBody = escapeJson(requestBody);
        // 将转义后的数据包装成流返回
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(escapedRequestBody.getBytes(StandardCharsets.UTF_8));

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {}

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    private String escapeJson(String input) {
        if (input == null) {
            return null;
        }

        try {
            Object jsonObject = objectMapper.readValue(input, Object.class);
            jsonObject = escapeJsonProperties(jsonObject);
            return objectMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            return input;
        }
    }

    private Object escapeJsonProperties(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof String) {
            return HtmlUtils.htmlEscape((String) object, StandardCharsets.UTF_8.name());
        } else if (object instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                entry.setValue(escapeJsonProperties(entry.getValue()));
            }
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            for (int i = 0; i < list.size(); i++) {
                list.set(i, escapeJsonProperties(list.get(i)));
            }
        } else if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            for (int i = 0; i < array.length; i++) {
                array[i] = escapeJsonProperties(array[i]);
            }
        }
        return object;
    }
}
