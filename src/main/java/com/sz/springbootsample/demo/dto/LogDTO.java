package com.sz.springbootsample.demo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 日志传输对象
 *
 * @author Yanghj
 * @date 1/2/2020
 */
@Data
@Accessors(chain = true)
public class LogDTO {
    private String logCode;
    private Integer logStep;
    private Integer adviceCount;
    private Boolean isThrowing;
    private Boolean isIgnoreTracing;
}
