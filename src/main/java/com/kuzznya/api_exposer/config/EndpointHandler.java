package com.kuzznya.api_exposer.config;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class EndpointHandler {
    private final Object service;
    private final Method method;
    private final Map<String, String> paramsMapping;

    public EndpointHandler(Object service, Method method, Map<String, String> paramsMapping) {
        this.service = service;
        this.method = method;
        this.paramsMapping = paramsMapping;
    }

    private List<Object> mapParams(MultiValueMap<String, String> requestParams,
                                   Collection<Parameter> parameters,
                                   @NonNull Map<String, String> paramsMapping) {
        return parameters.stream()
                .map(parameter -> parameter.getType().cast(
                        paramsMapping.get(parameter.getName()).startsWith("?") ?
                                (Collection.class.isAssignableFrom(parameter.getType()) ?
                                        requestParams.get(paramsMapping.get(parameter.getName()).substring(1)) :
                                        requestParams.getFirst(paramsMapping.get(parameter.getName()).substring(1))
                                ) :
                                paramsMapping.get(parameter.getName())
                        )
                )
                .collect(Collectors.toList());
    }

    private List<Object> mapParams(MultiValueMap<String, String> requestParams,
                                   Collection<Parameter> parameters) {
        return parameters.stream()
                .map(parameter -> parameter.getType().cast(
                        (requestParams.get(parameter.getName()).size() == 1 ||
                                !Collection.class.isAssignableFrom(parameter.getType())) ?
                                requestParams.getFirst(parameter.getName()) :
                                requestParams.get(parameter.getName())
                        )
                )
                .collect(Collectors.toList());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handle(@RequestParam MultiValueMap<String, String> requestParams)
            throws InvocationTargetException, IllegalAccessException {
        if (paramsMapping != null)
            return method.invoke(
                    service,
                    mapParams(requestParams, Arrays.asList(method.getParameters()), paramsMapping).toArray()
            );
        else
            return method.invoke(
                    service,
                    mapParams(requestParams, Arrays.asList(method.getParameters())).toArray()
            );
    }
}
