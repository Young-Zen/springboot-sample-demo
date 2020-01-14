package com.sz.springbootsample.demo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Demo模型
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Data
@TableName("t_boot_demo")
public class DemoPO {

    @TableId(type = IdType.AUTO)
    @TableField("pk_demo_id")
    private Long pkDemoId;

    @TableField("name")
    private String name;

    @TableField("age")
    private int age;

    @TableField("account")
    private double account;

    @TableField("create_time")
    private Date createTime;
}
