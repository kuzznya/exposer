package com.github.kuzznya.exposer.core.builder;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;

public class ExposerConfigurationBuilder extends RouteBuilder<ExposerConfigurationBuilder> {
    public ExposerConfiguration configure() {
        return new ExposerConfiguration(super.getRoutes(), super.getEndpoints(), super.getBean());
    }

    @Override
    ExposerConfigurationBuilder getThis() {
        return this;
    }
}
