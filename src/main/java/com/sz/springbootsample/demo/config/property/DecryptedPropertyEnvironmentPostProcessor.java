package com.sz.springbootsample.demo.config.property;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.CollectionUtils;

/**
 * @author Yanghj
 * @date 2024/4/20 16:00
 */
public class DecryptedPropertyEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PROPERTY_SOURCE_NAME = "DecryptedProperties";

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment, SpringApplication application) {
        PropertySource<?> propertySource =
                environment.getPropertySources().get(PROPERTY_SOURCE_NAME);
        if (Objects.nonNull(propertySource)) {
            return;
        }

        List<String> key = environment.getProperty("custom.decrypt.property.key", List.class);
        if (CollectionUtils.isEmpty(key)) {
            return;
        }

        String prefix = environment.getProperty("custom.decrypt.property.prefix", "PASS(");
        String suffix = environment.getProperty("custom.decrypt.property.suffix", ")");
        Properties props = new Properties();
        for (String k : key) {
            String property = environment.getProperty(k, String.class, "");
            if (!property.startsWith(prefix) || !property.endsWith(suffix)) {
                continue;
            }

            String encryptedMessage =
                    property.substring(prefix.length(), property.length() - suffix.length());
            String decryptStr = new String(Base64.getDecoder().decode(encryptedMessage));
            props.put(k, decryptStr);
        }

        if (props.size() > 0) {
            environment
                    .getPropertySources()
                    .addFirst(new PropertiesPropertySource(PROPERTY_SOURCE_NAME, props));
        }
    }
}
