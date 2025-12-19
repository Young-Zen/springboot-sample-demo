package com.sz.springbootsample.demo.annotation.validator;

import java.lang.invoke.MethodHandles;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

/**
 * @author Yanghj
 * @date 2025/12/19 15:38
 */
public class HtmlEncodingLengthValidator
        implements ConstraintValidator<HtmlEncodingLength, CharSequence> {

    private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

    private int min;
    private int max;

    @Override
    public void initialize(HtmlEncodingLength parameters) {
        min = parameters.min();
        max = parameters.max();
        validateParameters();
    }

    @Override
    public boolean isValid(
            CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        int length = HtmlUtils.htmlUnescape(value.toString()).length();
        return length >= min && length <= max;
    }

    private void validateParameters() {
        if (min < 0) {
            throw LOG.getMinCannotBeNegativeException();
        }
        if (max < 0) {
            throw LOG.getMaxCannotBeNegativeException();
        }
        if (max < min) {
            throw LOG.getLengthCannotBeNegativeException();
        }
    }
}
