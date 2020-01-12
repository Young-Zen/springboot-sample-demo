package com.sz.springbootsample.demo.controller;

import com.sz.springbootsample.demo.annotation.IgnoreTracing;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Yanghj
 * @date 1/12/2020
 */
@Controller
@Api(tags = "主页")
public class HomeController {

    @GetMapping("/")
    @IgnoreTracing
    @ApiOperation("设置主页")
    public String home() {
        return "redirect:swagger-ui.html";
    }
}
