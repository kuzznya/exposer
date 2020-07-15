package com.github.kuzznya.exposer.core.config;

import com.github.kuzznya.exposer.core.model.Endpoint;
import com.github.kuzznya.exposer.core.model.EndpointProperty;
import com.github.kuzznya.exposer.core.model.RouteProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExposerConfiguration {
    private List<RouteProperty> routes = Collections.emptyList();
    private List<EndpointProperty> endpoints = Collections.emptyList();
    private String bean;


    public List<Endpoint> getEndpoints() {
        return Stream.concat(
                constructEndpoints(endpoints, "", bean).stream(),
                routes.stream()
                        .map(routeProperty -> getEndpoints(routeProperty, routeProperty.getPath(), bean))
                        .flatMap(Collection::stream)
        ).collect(Collectors.toList());
    }

    private List<Endpoint> getEndpoints(RouteProperty routeProperty, @NonNull String parentPath, String beanName) {
        return Stream.concat(
                constructEndpoints(
                        routeProperty.getEndpoints(),
                        parentPath,
                        beanName
                ).stream(),

                routeProperty.getRoutes()
                        .stream()
                        .map(route -> getEndpoints(
                                route,
                                joinPath(parentPath, route.getPath()),
                                route.getBean() != null ? route.getBean() : beanName
                                )
                        )
                        .flatMap(Collection::stream)
        ).collect(Collectors.toList());
    }

    private List<Endpoint> constructEndpoints(List<EndpointProperty> properties,
                                              @NonNull String parentPath,
                                              @NonNull String beanName) {
        return properties
                .stream()
                .map(endpointProperty ->
                        endpointProperty.getEndpoint(
                                parentPath,
                                endpointProperty.getBean() != null ? endpointProperty.getBean() : beanName
                        )
                ).collect(Collectors.toList());
    }

    private String joinPath(@NonNull String parentPath, @NonNull String childPath) {
        parentPath = !parentPath.equals("/") ? parentPath : "";
        childPath = !childPath.equals("/") ? childPath : "";

        if (parentPath.endsWith("/") && childPath.startsWith("/"))
            return parentPath + childPath.substring(1);
        else if (!parentPath.endsWith("/") && !childPath.startsWith("/"))
            return parentPath + "/" + childPath;
        else
            return parentPath + childPath;
    }
}
