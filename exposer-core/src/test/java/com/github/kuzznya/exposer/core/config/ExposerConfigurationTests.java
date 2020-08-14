package com.github.kuzznya.exposer.core.config;

import com.github.kuzznya.exposer.core.model.RouteProperty;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExposerConfigurationTests {

    @Test
    public void nullAssert() {
        assertThrows(
                NullPointerException.class,
                () -> ExposerConfiguration.getEndpoints(new RouteProperty(), null, null)
        );

        assertThrows(
                NullPointerException.class,
                () -> ExposerConfiguration.constructEndpoints(Collections.emptySet(), null, null)
        );
    }

    @Test
    public void joinPathTest() {
        assertThrows(
                NullPointerException.class,
                () -> ExposerConfiguration.joinPath(null, null)
        );

        assertEquals(
                "/value1/value2",
                ExposerConfiguration.joinPath("/value1", "value2")
        );

        assertEquals(
                "/value2",
                ExposerConfiguration.joinPath("/", "value2")
        );

        assertEquals(
                "/value1/value2",
                ExposerConfiguration.joinPath("/value1", "/value2")
        );

        assertEquals(
                "/value1/value2",
                ExposerConfiguration.joinPath("/value1/", "/value2")
        );

        assertEquals(
                "/value1/",
                ExposerConfiguration.joinPath("/value1", "/")
        );
    }
}
