package com.sz.springbootsample.demo.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author Yanghj
 * @date 2024/6/24 16:36
 */
@Data
@Component
@ConfigurationProperties(prefix = "grpc")
public class GrpcProperties {

    private GrpcEndpoint server;

    private Client client;

    private boolean enableReflection;

    @Data
    public static class Client {
        private GrpcEndpoint stockQuote;
    }
}
