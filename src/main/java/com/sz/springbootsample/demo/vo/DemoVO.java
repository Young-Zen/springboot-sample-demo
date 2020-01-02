package com.sz.springbootsample.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
