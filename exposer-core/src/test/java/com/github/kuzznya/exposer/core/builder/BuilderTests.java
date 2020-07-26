package com.github.kuzznya.exposer.core.builder;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import com.github.kuzznya.exposer.core.model.Endpoint;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

public class BuilderTests {

    @Test
    public void exposerConfigurationBuilderTest() {
        ExposerConfiguration testConfig =
                ExposerConfiguration.builder()
                        .route("/test")
                            .bean("testBean")
                            .endpoint(RequestMethod.DELETE, "deleteMethod").param("value", "?val").and()
                            .endpoint(RequestMethod.GET, "testMethod").register()
                            .add()
                        .configure();
        assert testConfig.getEndpoints().contains(
                new Endpoint(
                        "/test",
                        RequestMethod.DELETE,
                        "testBean",
                        "deleteMethod",
                        Map.of("value", "?val")
                )
        );
    }
}
