package com.kuzznya.api_exposer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "exposer")
@Data
public class ExposerProperties {
    @Getter @Setter
    private List<Route> routes;
}
