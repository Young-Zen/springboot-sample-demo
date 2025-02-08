package com.sz.springbootsample.demo.enums;

import java.util.function.Function;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 脱敏策略，枚举类，针对不同的数据定制特定的策略
 *
 * @author Yanghj
 * @date 2024/10/24 19:10
 */
@Getter
@AllArgsConstructor
public enum DesensitizationStrategyEnum {
    /** 默认 */
    DEFAULT(s -> "***"),

    /** 用户名 */
    USERNAME(DesensitizedUtil::chineseName),

    /** 手机号 */
    PHONE(DesensitizedUtil::mobilePhone),

    /** 电子邮件 */
    EMAIL(DesensitizedUtil::email);

    /** 脱敏策略 */
    private final Function<String, String> desensitization;
}
