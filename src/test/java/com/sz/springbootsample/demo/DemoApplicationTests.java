package com.sz.springbootsample.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Demo应用程序测试类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
        // https://medium.com/@bubu.tripathy/testing-spring-boot-applications-c5d8212f6e72
    }
}
