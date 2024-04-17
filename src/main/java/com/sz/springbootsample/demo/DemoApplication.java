package com.sz.springbootsample.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Demo应用程序
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@SpringBootApplication(scanBasePackages = "com.sz")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
