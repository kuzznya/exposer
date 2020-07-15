package com.kuzznya.exposer_sample.config;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import com.github.kuzznya.exposer.core.model.EndpointProperty;
import com.github.kuzznya.exposer.core.model.RouteProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;

@Configuration
@Profile("code")
public class ExposerConfig {

    @Bean
    ExposerConfiguration exposerConfiguration() {
        return new ExposerConfiguration(
                List.of(
                        new RouteProperty(
                                "/test",
                                null,
                                List.of(
                                        new RouteProperty(
                                                "/v1",
                                                "TestService",
                                                Collections.emptyList(),
                                                List.of(
                                                        new EndpointProperty(
                                                                RequestMethod.GET,
                                                                null,
                                                                "getValue",
                                                                null
                                                        )
                                                )
                                        )
                                ),
                                Collections.emptyList()
                        )
                ),
                Collections.emptyList(),
                "TestService2"
        );
    }
}
