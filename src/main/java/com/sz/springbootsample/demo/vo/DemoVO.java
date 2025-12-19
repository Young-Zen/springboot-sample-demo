package com.sz.springbootsample.demo.vo;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.sz.springbootsample.demo.annotation.validator.HtmlEncodingLength;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Demo的视图对象
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "DemoVO 模型")
public class DemoVO {

    @ApiModelProperty("主键id")
    @Null(groups = Add.class, message = "插入时不能传输Id")
    @NotNull(groups = Update.class, message = "用户Id不能为空")
    private Long demoId;

    @ApiModelProperty(value = "昵称", required = true)
    @NotNull(
            groups = {Add.class, Update.class},
            message = "用户名不能为空")
    @HtmlEncodingLength(
            groups = {Add.class, Update.class},
            min = 5,
            max = 64,
            message = "用户名个数必须为5-64位")
    private String name;

    @ApiModelProperty("年龄")
    @Min(
            groups = {Add.class, Update.class},
            value = 1,
            message = "最小年龄为1")
    private Integer age;

    @ApiModelProperty("账户余额")
    @DecimalMin(
            groups = {Add.class, Update.class},
            value = "0",
            message = "最小金额为0")
    private BigDecimal account;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", example = "2020-01-01 00:00:00")
    private Date createTime;

    /** 继承Default类，可以在不指定 @Validated 的 group 时，使用所有默认校验方式。 */
    public interface Add extends Default {}

    public interface Update extends Default {}
}
