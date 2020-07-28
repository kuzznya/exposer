package com.github.kuzznya.exposer.core.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParameterEvaluator {
    private final EvaluationContext evaluationContext;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public ParameterEvaluator(MultiValueMap<String, String> requestParams,
                              Map<String, String> pathVariables) {
        RequestData requestData = new RequestData(requestParams, pathVariables);
        evaluationContext = new StandardEvaluationContext(requestData);
    }

    private Object evaluate(String expression) {
        return expressionParser
                .parseExpression(expression)
                .getValue(evaluationContext);
    }

    public Object getObjectValue(String query) {
        if (!query.startsWith("$(") || !query.endsWith(")"))
            throw new EvaluationException();

        return evaluate(query.substring(2, query.length() - 1));
    }

    public String getStringValue(String query) {
        query = query.replaceAll("[\\\\][$]", "__DOLLAR_SIGN__");

        Pattern pattern = Pattern.compile("[$]\\(.*\\)");
        Matcher matcher = pattern.matcher(query);

        while (matcher.find()) {
            String expression = matcher.group();
            Object result = evaluate(expression.substring(2, expression.length() - 1));
            if (!(result instanceof String))
                throw new EvaluationException("Cannot concat non-String result with String expression");

            query = matcher.replaceFirst((String) result);

            matcher = pattern.matcher(query);
        }

        return query.replaceAll("__DOLLAR_SIGN__", "$");
    }

    public Object getValue(String query) {
        query = query.strip();

        try {
            if (query.matches("[$]\\(.*\\)"))
                return getObjectValue(query);
            else
                return getStringValue(query);
        } catch (IllegalArgumentException ex) {
            throw new EvaluationException("Cannot evaluate query: " + query, ex);
        }
    }
}
