package com.github.kuzznya.exposer.core.builder;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;

public class BuilderTests {

    @Test
    public void exposerConfigurationBuilderTest() {
        System.out.println(
                ExposerConfiguration.builder()
                        .route("/test")
                            .bean("testBean")
                            .endpoint(RequestMethod.DELETE, "deleteMethod").param("value", "?val").and()
                            .endpoint(RequestMethod.GET, "testMethod").register()
                            .add()
                        .configure()
        );
    }
}
