package com.github.kuzznya.exposer.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteProperty {
    @NonNull
    private String path;
    private String bean;
    private Set<RouteProperty> routes = Collections.emptySet();
    private Set<EndpointProperty> endpoints = Collections.emptySet();
}
