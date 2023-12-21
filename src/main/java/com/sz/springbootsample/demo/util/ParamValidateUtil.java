package com.sz.springbootsample.demo.util;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

/**
 * @author Yanghj
 * @date 2023/6/25 16:02
 */
@Component
public class ParamValidateUtil {

    public void verifyParams(@Valid Object... args) {}
}
