package com.sz.springboot_sample.demo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@Data
@Accessors(chain = true)
public class DemoVO {
    private int age;
    private String name;
}
