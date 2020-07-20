package com.github.kuzznya.exposer.sample.config;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import com.github.kuzznya.exposer.core.config.ExposerConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExposerConfig implements ExposerConfigurer {
    @Override
    public ExposerConfiguration configureExposer() {
        return ExposerConfiguration.builder().configure();
    }
}
