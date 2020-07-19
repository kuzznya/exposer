package com.github.kuzznya.exposer.core.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface ExposerConfigurer {
    ExposerConfiguration configureExposer();
}
