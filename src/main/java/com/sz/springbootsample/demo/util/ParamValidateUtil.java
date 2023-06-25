package com.sz.springbootsample.demo.util;

import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * @author Yanghj
 * @date 2023/6/25 16:02
 */
@Component
public class ParamValidateUtil {

    public void verifyParams(@Valid Object... args) {
    }
}
