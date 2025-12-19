package com.sz.springbootsample.demo.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.sz.springbootsample.demo.annotation.Sensitive;
import com.sz.springbootsample.demo.annotation.validator.HtmlEncodingLength;
import com.sz.springbootsample.demo.annotation.validator.ValidPassword;
import com.sz.springbootsample.demo.enums.DesensitizationStrategyEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户
 *
 * @author Yanghj
 * @since 2020-03-16
 */
@Data
@ToString(exclude = {"password", "phone", "email"})
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(description = "用户")
public class UserVO {

    @ApiModelProperty(value = "主键 id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    @NotNull(message = "用户名不能为空")
    @HtmlEncodingLength(min = 5, max = 64, message = "用户名个数必须为5-64位")
    private String userName;

    @ApiModelProperty(value = "密码")
    @ValidPassword
    @Sensitive
    private String password;

    @ApiModelProperty(value = "生日", example = "2020-01-01")
    private LocalDate birthday;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    @Sensitive(strategy = DesensitizationStrategyEnum.PHONE)
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Email
    @Sensitive(strategy = DesensitizationStrategyEnum.EMAIL)
    private String email;

    @ApiModelProperty(value = "是否禁用")
    private Boolean isDisabled;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
