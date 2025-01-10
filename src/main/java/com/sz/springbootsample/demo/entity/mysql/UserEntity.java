package com.sz.springbootsample.demo.entity.mysql;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.sz.springbootsample.demo.config.mybatisplus.CryptoJacksonTypeHandler;

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
@TableName(value = "t_user", autoResultMap = true)
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_user_id", type = IdType.AUTO)
    private Long pkUserId;

    private String userName;

    @TableField(value = "password", typeHandler = CryptoJacksonTypeHandler.class)
    private String password;

    private LocalDate birthday;

    private String phone;

    private String email;

    private Boolean isDisabled;

    private Boolean isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
