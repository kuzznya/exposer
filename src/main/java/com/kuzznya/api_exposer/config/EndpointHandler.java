package com.kuzznya.api_exposer.config;

import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndpointHandler {
    private final Object service;
    private final Method method;

    public EndpointHandler(Object service, Method method) {
        this.service = service;
        this.method = method;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handle(@RequestParam MultiValueMap<String, String> requestParams, HttpServletRequest request) throws InvocationTargetException, IllegalAccessException {
        Parameter[] methodParams = method.getParameters();
        List<Object> args = new ArrayList<>();
        for (Parameter param : methodParams) {
            Object arg;
            if (requestParams.get(param.getName()).size() == 1)
                arg =  requestParams.getFirst(param.getName());
            else
                arg = requestParams.get(param.getName());
            args.add(param.getType().cast(arg));
        }
        return method.invoke(service, args.toArray());
    }
}
