package com.github.kuzznya.exposer.core.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultExposerConfigurer implements ExposerConfigurer {
    @Override
    public ExposerConfiguration configureExposer() {
        return ExposerConfiguration.builder().configure();
    }
}
