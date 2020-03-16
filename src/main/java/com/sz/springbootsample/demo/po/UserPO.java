package com.sz.springbootsample.demo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author Yanghj
 * @since 2020-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class UserPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_user_id", type = IdType.AUTO)
    private Long pkUserId;

    private String userName;

    private String password;

    private LocalDate birthday;

    private String phone;

    private String email;

    private Boolean isDisabled;

    private Boolean isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
