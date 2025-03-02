package com.sz.springbootsample.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码及对应消息枚举类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {
    /** 通用操作成功 */
    OK("0", "操作成功"),

    /** 通用操作失败 */
    FAIL("1", "操作失败"),

    /** 参数校验失败 */
    ARGUMENT_VALID_FAIL("2", "参数校验失败"),

    /** 无权访问 */
    FORBIDDEN("403", "禁止访问"),

    /** 资源不存在 */
    NOT_FOUND("404", "资源不存在"),

    /** 系统内部异常 */
    INTERNAL_SERVER_ERROR("500", "系统内部异常");

    /** 编码 */
    private String code;

    /** 消息 */
    private String msg;
}
