package com.github.kuzznya.exposer.core;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import com.github.kuzznya.exposer.core.config.ExposerConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
public class ExposerConfig implements ExposerConfigurer {
    @Override
    public ExposerConfiguration configureExposer() {
        return ExposerConfiguration.builder()
                .route("/test")
                .bean("exposingService")
                .endpoint(RequestMethod.DELETE, "deleteMethod").param("value", "?val").and()
                .endpoint(RequestMethod.GET, "testMethod").register()
                .add()
                .configure();
    }
}
