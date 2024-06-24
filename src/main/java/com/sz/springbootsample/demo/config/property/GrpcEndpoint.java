package com.sz.springbootsample.demo.config.property;

import lombok.Data;

/**
 * @author Yanghj
 * @date 2024/6/24 16:36
 */
@Data
public class GrpcEndpoint {

    private String host;

    private int port;
}
