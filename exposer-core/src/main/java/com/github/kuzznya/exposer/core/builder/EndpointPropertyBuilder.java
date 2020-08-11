package com.github.kuzznya.exposer.core.builder;

import com.github.kuzznya.exposer.core.model.EndpointProperty;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EndpointPropertyBuilder<ParentBuilderClass extends RouteBuilder<?>> {
    private final ParentBuilderClass parentBuilder;

    @NonNull
    private final RequestMethod httpMethod;

    private String bean;
    @NonNull
    private final String beanMethod;

    private Map<String, String> params;

    private Class<?> requestBodyClass;

    public EndpointPropertyBuilder(ParentBuilderClass parentBuilder, RequestMethod httpMethod, String beanMethod) {
        this.parentBuilder = parentBuilder;
        this.httpMethod = httpMethod;
        this.beanMethod = beanMethod;
    }

    public EndpointPropertyBuilder<ParentBuilderClass> param(String key, String value) {
        if (params == null)
            params = new HashMap<>();
        params.put(key, value);
        return this;
    }

    public EndpointPropertyBuilder<ParentBuilderClass> bean(String bean) {
        this.bean = bean;
        return this;
    }

    public EndpointPropertyBuilder<ParentBuilderClass> requestBodyClass(Class<?> requestBodyClass) {
        this.requestBodyClass = requestBodyClass;
        return this;
    }

    public ParentBuilderClass register() {
        parentBuilder.addEndpoint(
                new EndpointProperty(
                        httpMethod,
                        Optional.ofNullable(bean).orElse(parentBuilder.getBean()),
                        beanMethod,
                        params,
                        requestBodyClass
                )
        );
        return parentBuilder;
    }

    public ParentBuilderClass and() {
        return register();
    }

}
