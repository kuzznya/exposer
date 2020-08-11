package com.github.kuzznya.exposer.core.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

public class ParameterEvaluator {
    private final EvaluationContext evaluationContext;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    private RequestData<?> requestData;

    private final Map<String, Expression> parsedExpressions = new HashMap<>();

    public ParameterEvaluator() {
        evaluationContext = SimpleEvaluationContext
                .forReadOnlyDataBinding()
                .build();
    }

    public <T> void setRequestData(MultiValueMap<String, String> requestParams,
                               Map<String, String> pathVariables,
                               T body,
                               Map<String, Object> bodyData) {
        requestData = new RequestData<>(requestParams, pathVariables, body, bodyData);
    }

    public Object evaluate(String expression) {
        try {
            if (!parsedExpressions.containsKey(expression))
                parsedExpressions.put(
                        expression,
                        expressionParser.parseExpression(expression)
                );

            return parsedExpressions
                    .get(expression)
                    .getValue(evaluationContext, requestData);
        } catch (Exception ex) {
            throw new EvaluationException("Cannot evaluate expression: " + expression, ex);
        }
    }
}
