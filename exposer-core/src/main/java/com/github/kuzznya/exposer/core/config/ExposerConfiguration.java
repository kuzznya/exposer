package com.github.kuzznya.exposer.core.config;

import com.github.kuzznya.exposer.core.builder.ExposerConfigurationBuilder;
import com.github.kuzznya.exposer.core.model.Endpoint;
import com.github.kuzznya.exposer.core.model.EndpointProperty;
import com.github.kuzznya.exposer.core.model.RouteProperty;
import lombok.NonNull;
import lombok.Value;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
public class ExposerConfiguration {
    Set<RouteProperty> routes;
    Set<EndpointProperty> endpoints;
    String bean;


    public Set<Endpoint> getEndpoints() {
        return Stream.concat(
                constructEndpoints(endpoints, "", bean).stream(),
                routes.stream()
                        .map(routeProperty -> getEndpoints(routeProperty, routeProperty.getPath(), bean))
                        .flatMap(Collection::stream)
        ).collect(Collectors.toSet());
    }

    private Set<Endpoint> getEndpoints(RouteProperty routeProperty, @NonNull String parentPath, String beanName) {
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
                                Optional.ofNullable(route.getBean()).orElse(beanName)
                                )
                        )
                        .flatMap(Collection::stream)
        ).collect(Collectors.toSet());
    }

    private Set<Endpoint> constructEndpoints(Set<EndpointProperty> properties,
                                              @NonNull String parentPath,
                                              String beanName) {
        return properties
                .stream()
                .map(endpointProperty ->
                        endpointProperty.getEndpoint(
                                parentPath,
                                Optional.ofNullable(endpointProperty.getBean()).orElse(beanName)
                        )
                ).collect(Collectors.toSet());
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

    public static ExposerConfigurationBuilder builder() {
        return new ExposerConfigurationBuilder();
    }
}
