package com.github.kuzznya.exposer.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteProperty {
    @NonNull
    private String path;
    private String bean;
    private List<RouteProperty> routes = Collections.emptyList();
    private List<EndpointProperty> endpoints = Collections.emptyList();
}
