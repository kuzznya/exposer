package com.github.kuzznya.exposer.core.builder;

import com.github.kuzznya.exposer.core.model.RouteProperty;

public class RoutePropertyBuilder<ParentBuilderClass extends RouteBuilder<?>>
        extends RouteBuilder<RoutePropertyBuilder<ParentBuilderClass>> {
    private final ParentBuilderClass parentBuilder;

    private final String path;

    public RoutePropertyBuilder(ParentBuilderClass parentBuilder, String path) {
        this.parentBuilder = parentBuilder;
        this.path = path;
    }

    public ParentBuilderClass add() {
        parentBuilder.addRoute(
                new RouteProperty(
                        this.path,
                        super.getBean(),
                        super.getRoutes(),
                        super.getEndpoints()
                )
        );
        return parentBuilder;
    }

    public ParentBuilderClass and() {
        return add();
    }

    @Override
    RoutePropertyBuilder<ParentBuilderClass> getThis() {
        return this;
    }
}
