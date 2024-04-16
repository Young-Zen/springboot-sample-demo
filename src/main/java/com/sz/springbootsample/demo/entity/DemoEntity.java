package com.sz.springbootsample.demo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Demo模型
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Data
@Accessors(chain = true)
@TableName("t_boot_demo")
public class DemoEntity {

    @TableId(type = IdType.AUTO)
    @TableField("pk_demo_id")
    private Long pkDemoId;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("account")
    private BigDecimal account;

    @TableField("create_time")
    private Date createTime;
}
