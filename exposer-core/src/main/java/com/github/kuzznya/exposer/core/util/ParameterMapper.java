package com.github.kuzznya.exposer.core.util;

import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ParameterMapper {
    public static Object mapCollectionResult(MethodParameter parameter, Collection<?> result) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (Collection.class.isAssignableFrom(parameter.getParameterType()))
            return parameter.getParameterType().getMethod("copyOf", Collection.class).invoke(null, result);
        else
            return ((Collection<?>) result).stream()
                    .findFirst()
                    .orElse(null);
    }

    public static Object mapSingleResult(MethodParameter parameter, Object result) throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (result instanceof Collection)
            throw new IllegalArgumentException("Cannot map collection as a single result");

        if (Collection.class.isAssignableFrom(parameter.getParameterType()))
            return mapCollectionResult(parameter, Collections.singletonList(result));
        else
            return result;
    }

    public static List<Object> mapParams(ParameterEvaluator evaluator,
                                   Collection<MethodParameter> parameters,
                                   @NonNull Map<String, String> paramsMapping) {
        return parameters.parallelStream()
                .map(parameter -> {
                    Object result = evaluator.evaluate(paramsMapping.get(parameter.getParameterName()));

                    try {
                        if (result instanceof Collection)
                            return mapCollectionResult(parameter, (Collection<?>) result);
                        else
                            return mapSingleResult(parameter, result);
                    } catch (ReflectiveOperationException ex) {
                        throw new EvaluationException("Cannot handle params mapping", ex);
                    }
                })
                .collect(Collectors.toList());
    }

    public static List<Object> mapParams(Collection<MethodParameter> parameters,
                                   MultiValueMap<String, String> requestParams) {
        return parameters.parallelStream()
                .map(parameter -> !Collection.class.isAssignableFrom(parameter.getParameterType()) ?
                        requestParams.getFirst(Objects.requireNonNull(parameter.getParameterName())) :
                        requestParams.get(parameter.getParameterName())
                )
                .collect(Collectors.toList());
    }
}
