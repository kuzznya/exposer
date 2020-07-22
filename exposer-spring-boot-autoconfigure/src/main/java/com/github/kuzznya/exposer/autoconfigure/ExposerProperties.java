package com.github.kuzznya.exposer.autoconfigure;

import com.github.kuzznya.exposer.core.model.EndpointProperty;
import com.github.kuzznya.exposer.core.model.RouteProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

@ConfigurationProperties(prefix = "exposer")
@Data
public class ExposerProperties {
    private Set<RouteProperty> routes = Collections.emptySet();
    private Set<EndpointProperty> endpoints = Collections.emptySet();
    private String bean;
}
