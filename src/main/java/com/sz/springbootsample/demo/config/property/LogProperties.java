package com.sz.springbootsample.demo.config.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
@Data
@ConditionalOnProperty(name = "custom.log.enable", matchIfMissing = true)
@Component
@ConfigurationProperties(prefix = "custom.log")
public class LogProperties {
    private Boolean enable = Boolean.TRUE;
    private Boolean param = Boolean.TRUE;
    private Boolean result = Boolean.TRUE;
}
