package com.github.kuzznya.exposer.autoconfigure.config;

import com.github.kuzznya.exposer.autoconfigure.model.ExposerProperties;
import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import com.github.kuzznya.exposer.core.config.ExposerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ExposerConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class ExposerAutoConfiguration {

//    @Bean
//    @ConditionalOnMissingBean
//    public ExposerConfiguration exposerConfiguration() {
//        return new ExposerConfiguration(
//                properties.getRoutes(),
//                properties.getEndpoints(),
//                properties.getBean()
//        );
//    }

    @Configuration
    @EnableWebMvc
    @ConditionalOnWebApplication
    @EnableConfigurationProperties(ExposerProperties.class)
    @ConditionalOnMissingBean(value = ExposerConfigurer.class)
    protected static class ExposerAutoConfigurer implements ExposerConfigurer {

        private final ExposerProperties properties;

        public ExposerAutoConfigurer(ExposerProperties properties) {
            this.properties = properties;
        }

        @Override
        public ExposerConfiguration configureExposer() {
            return new ExposerConfiguration(
                    properties.getRoutes(),
                    properties.getEndpoints(),
                    properties.getBean()
            );
        }
    }
}
