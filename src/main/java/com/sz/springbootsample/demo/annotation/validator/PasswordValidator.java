package com.sz.springbootsample.demo.annotation.validator;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Yanghj
 * @date 2023/6/25 15:57
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private Pattern regex = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,20}$");

    @Override
    public void initialize(ValidPassword constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return regex.matcher(value).matches();
    }
}
