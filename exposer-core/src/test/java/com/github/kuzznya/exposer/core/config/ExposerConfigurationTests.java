package com.github.kuzznya.exposer.core.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExposerConfigurationTests {

    @Test
    public void joinPathTest() {
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
