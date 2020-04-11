package com.sz.springbootsample.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 给 k8s 提供就绪检测
 *
 * @author Yanghj
 * @date 4/11/2020
 */
@RestController
@Api(tags = "就绪检测")
public class PingController {

    @GetMapping("/ping")
    @ApiOperation("Offer httpGet readiness probe for k8s")
    public String csrf() {
        return "PONG";
    }
}
