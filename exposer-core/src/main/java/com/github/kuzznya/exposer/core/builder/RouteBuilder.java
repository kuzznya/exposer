package com.github.kuzznya.exposer.core.builder;

import com.github.kuzznya.exposer.core.model.EndpointProperty;
import com.github.kuzznya.exposer.core.model.RouteProperty;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class RouteBuilder<BuilderClass extends RouteBuilder<BuilderClass>> {
    @Getter
    private final Set<RouteProperty> routes = new LinkedHashSet<>();
    @Getter
    private final Set<EndpointProperty> endpoints = new LinkedHashSet<>();
    @Getter
    private String bean;

    void addRoute(RouteProperty routeProperty) {
        routes.add(routeProperty);
    }

    void addEndpoint(EndpointProperty endpointProperty) {
        endpoints.add(endpointProperty);
    }

    abstract BuilderClass getThis();

    public RoutePropertyBuilder<BuilderClass> route(String path) {
        return new RoutePropertyBuilder<>(getThis(), path);
    }

    public EndpointPropertyBuilder<BuilderClass> endpoint(RequestMethod httpMethod, String beanMethod) {
        return new EndpointPropertyBuilder<>(getThis(), httpMethod, beanMethod);
    }

    public BuilderClass bean(String bean) {
        this.bean = bean;
        return getThis();
    }
}
