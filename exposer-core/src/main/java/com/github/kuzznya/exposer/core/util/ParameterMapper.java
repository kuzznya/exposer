package com.github.kuzznya.exposer.core.util;

import lombok.NonNull;
import org.springframework.core.MethodParameter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParameterMapper {

    private static Object mapResult(MethodParameter parameter, Collection<?> result)
            throws ReflectiveOperationException {
        if (Collection.class.isAssignableFrom(parameter.getParameterType()))
            return parameter.getParameterType()
                    .getMethod("copyOf", Collection.class)
                    .invoke(null, result);
        else
            return ((Collection<?>) result).stream()
                    .findFirst()
                    .orElse(null);
    }

    public static Object mapResult(MethodParameter parameter, Object result) {
        try {
            if (result instanceof Collection)
                return mapResult(parameter, (Collection<?>) result);

            if (Collection.class.isAssignableFrom(parameter.getParameterType()))
                return mapResult(parameter, Collections.singletonList(result));
            else
                return result;
        } catch (ReflectiveOperationException ex) {
            throw new EvaluationException("Cannot map value: " + result.toString() + " to parameter", ex);
        }
    }

    public static List<Object> mapParams(ParameterEvaluator evaluator,
                                   Collection<MethodParameter> parameters,
                                   @NonNull Map<String, String> paramsMapping) {
        return parameters.parallelStream()
                .map(parameter ->
                        mapResult(
                                parameter,
                                evaluator.evaluate(paramsMapping.get(parameter.getParameterName()))
                        )
                )
                .collect(Collectors.toList());
    }
}
