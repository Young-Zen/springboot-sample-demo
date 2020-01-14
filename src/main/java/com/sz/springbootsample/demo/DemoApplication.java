package com.sz.springbootsample.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Demo应用程序
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@SpringBootApplication(scanBasePackages = "com.sz")
@MapperScan({"com.sz.**.dao"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
