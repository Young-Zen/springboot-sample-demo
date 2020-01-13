package com.sz.springbootsample.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yanghj
 * @date 1/13/2020
 */
@RestController
@ConditionalOnProperty(name = {"custom.swagger.enable", "custom.swagger.csrf.enable"}, havingValue = "true")
@Api(tags = "设置csrf token")
public class CsrfController {

    @GetMapping("/csrf")
    @ApiOperation("Define an endpoint used to fetch csrf token for swagger-ui.html")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}