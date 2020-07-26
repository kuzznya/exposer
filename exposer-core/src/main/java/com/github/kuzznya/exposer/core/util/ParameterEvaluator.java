package com.github.kuzznya.exposer.core.util;

import lombok.AllArgsConstructor;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class ParameterEvaluator {
    private final MultiValueMap<String, String> requestParams;
    private final Map<String, String> pathVariables;

    private static String getKey(String query) {
        return getKey(query, 0);
    }

    private static String getKey(String query, int keyIndex) {
        Pattern pattern = Pattern.compile("\\[\\w+]");
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            String result = matcher.group(keyIndex);
            return result.substring(1, result.length() - 1).strip();
        }
        else
            throw new EvaluationException();
    }

    private Object getParams(String query) {
        if (!query.matches("params(\\[\\w+](\\[\\d+])?)?"))
            throw new IllegalArgumentException();

        if (query.equals("params"))
            return requestParams;

        if (query.matches("params\\[\\w+]"))
            return requestParams.get(getKey(query));
        else
            return requestParams
                    .get(getKey(query, 0))
                    .get(Integer.parseInt(getKey(query, 1)));
    }

    private String getPathVariable(String query) {
        if (!query.matches("path\\[\\w+]"))
            throw new IllegalArgumentException();

        return pathVariables.get(getValue(getKey(query)).toString());
    }

    private Object evaluate(String query) {
        if (query.startsWith("params"))
            return getParams(query);
        else if (query.startsWith("path"))
            return getPathVariable(query);
        else if (query.matches("\\{\\+}"))
            return Stream.of(query.substring(1, query.length() - 1).split(","))
                    .map(String::strip)
                    .map(this::getValue)
                    .collect(Collectors.toUnmodifiableList());
        else
            throw new EvaluationException("Cannot evaluate expression: " + query);
    }

    public Object getObjectValue(String expression) {
        if (!expression.startsWith("$(") || !expression.endsWith(")"))
            throw new EvaluationException();

        return evaluate(expression.substring(2, expression.length() - 1));
    }

    public String getStringValue(String expression) {
        expression = expression.replaceAll("[\\\\][$]", "__DOLLAR_SIGN__");

        Pattern pattern = Pattern.compile("[$]\\(.*\\)");
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            Object result = evaluate(matcher.group());
            if (!(result instanceof String))
                throw new EvaluationException("Cannot concat non-String result with String expression");

            matcher = pattern.matcher(
                    matcher.replaceFirst((String) result)
            );
        }

        return expression.replaceAll("__DOLLAR_SIGN__", "$");
    }

    public Object getValue(String expression) {
        expression = expression.strip();

        try {
            if (expression.matches("[$]\\(.*\\)"))
                return getObjectValue(expression);
            else
                return getStringValue(expression);
        } catch (IllegalArgumentException ex) {
            throw new EvaluationException("Cannot evaluate expression: " + expression, ex);
        }
    }
}
