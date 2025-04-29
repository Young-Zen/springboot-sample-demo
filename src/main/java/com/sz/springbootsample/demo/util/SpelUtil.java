package com.sz.springbootsample.demo.util;

import java.util.Map;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

/**
 * SpEL 工具类
 *
 * @author Yanghj
 * @date 2025/4/29 11:39
 */
public class SpelUtil {

    private static class SpelUtilHolder {
        private static final SpelUtil INSTANCE = new SpelUtil();
    }

    private ExpressionParser parser;

    public SpelUtil() {
        // 创建表达式解析器
        parser = new SpelExpressionParser();
    }

    public static final SpelUtil getInstance() {
        return SpelUtil.SpelUtilHolder.INSTANCE;
    }

    public <T> T getValue(
            Map<String, Object> contextMap, String expressionString, Class<T> desiredResultType)
            throws EvaluationException {
        // 解析表达式
        Expression expression = parser.parseExpression(expressionString);
        // 创建上下文
        SimpleEvaluationContext context =
                SimpleEvaluationContext.forReadOnlyDataBinding()
                        .withInstanceMethods()
                        .withRootObject(contextMap)
                        .build();
        // 计算表达式并获取结果
        return expression.getValue(context, desiredResultType);
    }

    public Boolean getBooleanValue(Map<String, Object> contextMap, String expressionString)
            throws EvaluationException {
        return this.getValue(contextMap, expressionString, Boolean.class);
    }
}
