package com.github.kuzznya.exposer.core;

import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class EndpointHandler {
    private final Object service;
    private final Method method;
    private final Map<String, String> paramsMapping;

    private final ParameterNameDiscoverer paramsDiscoverer;

    public EndpointHandler(Object service, Method method, Map<String, String> paramsMapping,
                           ParameterNameDiscoverer paramsDiscoverer) {
        this.service = service;
        this.method = method;
        this.paramsMapping = paramsMapping;
        this.paramsDiscoverer = paramsDiscoverer;
    }

    private List<Object> mapParams(MultiValueMap<String, String> requestParams,
                                   Collection<MethodParameter> parameters,
                                   @NonNull Map<String, String> paramsMapping) {
        return parameters.stream()
                .map(parameter -> parameter.getParameterType().cast(
                        paramsMapping.get(parameter.getParameterName()).startsWith("?") ?
                                (Collection.class.isAssignableFrom(parameter.getParameterType()) ?
                                        requestParams.get(paramsMapping.get(parameter.getParameterName()).substring(1)) :
                                        requestParams.getFirst(paramsMapping.get(parameter.getParameterName()).substring(1))
                                ) :
                                paramsMapping.get(parameter.getParameterName())
                        )
                )
                .collect(Collectors.toList());
    }

    private List<Object> mapParams(MultiValueMap<String, String> requestParams,
                                   Collection<MethodParameter> parameters) {
        return parameters.stream()
                .map(parameter -> parameter.getParameterType().cast(
                        (requestParams.get(parameter.getParameterName()).size() == 1 ||
                                !Collection.class.isAssignableFrom(parameter.getParameterType())) ?
                                requestParams.getFirst(Objects.requireNonNull(parameter.getParameterName())) :
                                requestParams.get(parameter.getParameterName())
                        )
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
                         @PathVariable Map<String, String> pathVariables)
            throws InvocationTargetException, IllegalAccessException {
        if (paramsMapping != null)
            return method.invoke(
                    service,
                    mapParams(requestParams, getMethodParameters(method), paramsMapping).toArray()
            );
        else
            return method.invoke(
                    service,
                    mapParams(requestParams, getMethodParameters(method)).toArray()
            );
    }
}
