package com.sz.springbootsample.demo.config.security.xss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author Yanghj
 * @date 2025/12/19 17:40
 */
public class XssHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private ObjectMapper objectMapper;
    private ByteArrayOutputStream outputStream;

    public XssHttpServletResponseWrapper(HttpServletResponse response, ObjectMapper objectMapper) {
        super(response);
        this.objectMapper = objectMapper;
        this.outputStream = new ByteArrayOutputStream();
    }

    public byte[] getResponseData() throws IOException {
        this.flushBuffer();

        String responseBody = IoUtil.toStr(outputStream, StandardCharsets.UTF_8);
        if (JSONUtil.isTypeJSON(responseBody)) {
            String escapedResponseBody = unEscapeJson(responseBody);
            return escapedResponseBody.getBytes(StandardCharsets.UTF_8);
        }

        return outputStream.toByteArray();
    }

    @Override
    public void flushBuffer() throws IOException {
        if (this.outputStream != null) {
            this.outputStream.flush();
        }
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(outputStream);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {}

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        };
    }

    private String unEscapeJson(String input) {
        if (input == null) {
            return null;
        }
        try {
            Object jsonObject = objectMapper.readValue(input, Object.class);
            jsonObject = unEscapeJsonProperties(jsonObject);
            return objectMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            return input;
        }
    }

    private Object unEscapeJsonProperties(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return HtmlUtils.htmlUnescape((String) object);
        } else if (object instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                entry.setValue(unEscapeJsonProperties(entry.getValue()));
            }
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            for (int i = 0; i < list.size(); i++) {
                list.set(i, unEscapeJsonProperties(list.get(i)));
            }
        } else if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            for (int i = 0; i < array.length; i++) {
                array[i] = unEscapeJsonProperties(array[i]);
            }
        }
        return object;
    }
}
