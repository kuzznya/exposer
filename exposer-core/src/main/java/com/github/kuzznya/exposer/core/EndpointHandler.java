package com.github.kuzznya.exposer.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuzznya.exposer.core.util.EvaluationException;
import com.github.kuzznya.exposer.core.util.ParameterEvaluator;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class EndpointHandler {
    private final Object service;
    private final Method method;
    private final Map<String, String> paramsMapping;

    private final ParameterNameDiscoverer paramsDiscoverer;

    private final ParameterEvaluator evaluator;

    private final Class<?> requestBodyClass;

    public EndpointHandler(Object service, Method method, Map<String, String> paramsMapping,
                           ParameterNameDiscoverer paramsDiscoverer, Class<?> requestBodyClass) {
        this.service = service;
        this.method = method;
        this.paramsMapping = paramsMapping;
        this.paramsDiscoverer = paramsDiscoverer;
        this.evaluator = new ParameterEvaluator();
        this.requestBodyClass = requestBodyClass;
    }

    private List<Object> mapParams(Collection<MethodParameter> parameters,
                                   @NonNull Map<String, String> paramsMapping) {
        return parameters.stream()
                .map(parameter -> {
                    Object result = evaluator.evaluate(paramsMapping.get(parameter.getParameterName()));

                    if (result instanceof Collection &&
                            !Collection.class.isAssignableFrom(parameter.getParameterType()))
                        return ((Collection<?>) result).stream()
                                .findFirst()
                                .orElseThrow(EvaluationException::new);
                    else
                        return result;
                })
                .collect(Collectors.toList());
    }

    private List<Object> mapParams(Collection<MethodParameter> parameters,
                                   MultiValueMap<String, String> requestParams) {
        return parameters.stream()
                .map(parameter -> (requestParams.get(parameter.getParameterName()).size() == 1 ||
                                !Collection.class.isAssignableFrom(parameter.getParameterType())) ?
                        requestParams.getFirst(Objects.requireNonNull(parameter.getParameterName())) :
                        requestParams.get(parameter.getParameterName())
                )
                .collect(Collectors.toList());
    }

    private List<MethodParameter> getMethodParameters(Method method) {
        List<MethodParameter> methodParameters = new ArrayList<>();
        for (int i = 0; i < method.getParameters().length; i++) {
            MethodParameter parameter = new MethodParameter(method, i);
            parameter.initParameterNameDiscovery(paramsDiscoverer);
            methodParameters.add(parameter);
        }
        return methodParameters;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handle(@RequestParam MultiValueMap<String, String> requestParams,
                         @PathVariable Map<String, String> pathVariables,
                         @RequestBody(required = false) Map<String, Object> requestBodyData)
            throws InvocationTargetException, IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();

        if (requestBodyClass != null)
            evaluator.setRequestData(requestParams, pathVariables,
                    mapper.convertValue(requestBodyData, requestBodyClass), requestBodyData);
        else
            evaluator.setRequestData(requestParams, pathVariables,
                    requestBodyData, requestBodyData);

        if (paramsMapping != null)
            return method.invoke(
                    service,
                    mapParams(getMethodParameters(method), paramsMapping).toArray()
            );
        else
            return method.invoke(
                    service,
                    mapParams(getMethodParameters(method), requestParams).toArray()
            );
    }
}
