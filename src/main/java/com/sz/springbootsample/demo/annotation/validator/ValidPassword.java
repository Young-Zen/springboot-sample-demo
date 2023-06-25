package com.sz.springbootsample.demo.annotation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yanghj
 * @date 2023/6/25 15:55
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "密码必须包含数字、字母和特殊字符，且长度在6到20之间";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
