package com.kuzznya.api_exposer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;

@Data
@NoArgsConstructor
public class RouteProperty {
    @NonNull
    private String path;
    private String bean;
    private List<RouteProperty> routes = Collections.emptyList();
    private List<EndpointProperty> endpoints = Collections.emptyList();
}
