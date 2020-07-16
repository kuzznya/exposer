package com.github.kuzznya.exposer.autoconfigure.model;

import com.github.kuzznya.exposer.core.model.EndpointProperty;
import com.github.kuzznya.exposer.core.model.RouteProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "exposer")
@Data
public class ExposerProperties {
    private List<RouteProperty> routes = Collections.emptyList();
    private List<EndpointProperty> endpoints = Collections.emptyList();
    private String bean;
}
