package com.sz.springboot_sample.demo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@Data
@Accessors(chain = true)
public class DemoVO {
    private String name;
    private int age;
    private double account;
    private Date createTime;
}
