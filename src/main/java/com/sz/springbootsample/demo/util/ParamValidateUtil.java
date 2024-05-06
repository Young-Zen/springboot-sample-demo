package com.sz.springbootsample.demo.util;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author Yanghj
 * @date 2023/6/25 16:02
 */
public class ParamValidateUtil {

    private static class ParamValidateUtilHolder {
        private static final ParamValidateUtil INSTANCE = new ParamValidateUtil();
    }

    private Validator validator;

    private ParamValidateUtil() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static final ParamValidateUtil getInstance() {
        return ParamValidateUtil.ParamValidateUtilHolder.INSTANCE;
    }

    public <T> void verifyParams(T object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
