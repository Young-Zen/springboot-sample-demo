package com.sz.springbootsample.demo.serializer;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import com.sz.springbootsample.demo.annotation.Sensitive;
import com.sz.springbootsample.demo.enums.DesensitizationStrategyEnum;

/**
 * 自定义序列化注解实现，用于敏感数据脱敏
 *
 * @author Yanghj
 * @date 2024/10/24 19:17
 */
public class SensitiveJsonSerializer extends JsonSerializer<String>
        implements ContextualSerializer {

    private DesensitizationStrategyEnum sensitiveStrategy;

    @Override
    public void serialize(
            String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(sensitiveStrategy.getDesensitization().apply(s));
    }

    @Override
    public JsonSerializer<?> createContextual(
            SerializerProvider serializerProvider, BeanProperty beanProperty)
            throws JsonMappingException {
        Sensitive annotation = beanProperty.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation)
                && Objects.equals(String.class, beanProperty.getType().getRawClass())) {
            this.sensitiveStrategy = annotation.strategy();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
