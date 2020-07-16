package com.github.kuzznya.exposer.autoconfigure.config;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import com.github.kuzznya.exposer.autoconfigure.model.ExposerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ExposerConfiguration.class)
@EnableConfigurationProperties(ExposerProperties.class)
public class ExposerAutoConfiguration {

    private final ExposerProperties properties;

    public ExposerAutoConfiguration(ExposerProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExposerConfiguration exposerConfiguration() {
        return new ExposerConfiguration(
                properties.getRoutes(),
                properties.getEndpoints(),
                properties.getBean()
        );
    }
}
