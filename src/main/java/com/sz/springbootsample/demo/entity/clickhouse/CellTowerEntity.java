package com.sz.springbootsample.demo.entity.clickhouse;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * @author Yanghj
 * @date 2024/4/16 16:50
 */
@Data
@TableName("cell_towers")
public class CellTowerEntity {

    @TableField("radio")
    private String radio;

    @TableField("mcc")
    private Integer mcc;

    @TableField("net")
    private Integer net;

    @TableField("area")
    private Integer area;

    @TableField("cell")
    private Long cell;

    @TableField("unit")
    private Integer unit;

    @TableField("lon")
    private Double lon;

    @TableField("lat")
    private Double lat;

    @TableField("range")
    private Integer range;

    @TableField("samples")
    private Integer samples;

    @TableField("changeable")
    private Integer changeable;

    @TableField("created")
    private LocalDateTime created;

    @TableField("updated")
    private LocalDateTime updated;

    @TableField("averageSignal")
    private Integer averageSignal;
}
