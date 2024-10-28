package com.sz.springbootsample.demo.enums;

import java.util.function.Function;

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
public enum SensitiveStrategyEnum {
    /** 默认 */
    DEFAULT(s -> "***"),
    /** 用户名 */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

    /** 身份证 */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1***$2")),

    /** 手机号 */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

    /** 电子邮件 */
    EMAIL(s -> s.replaceAll("^(\\w)(\\w+)(\\w)(@.*)$", "$1***$3$4")),

    /** 地址 */
    ADDRESS(s -> s.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1***$2***"));

    /** 脱敏策略 */
    private final Function<String, String> desensitization;
}
