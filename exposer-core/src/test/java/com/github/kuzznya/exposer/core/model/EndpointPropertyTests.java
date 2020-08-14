package com.github.kuzznya.exposer.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndpointPropertyTests {

    @Test
    void getEndpointNullAssertion() {
        EndpointProperty property = new EndpointProperty();
        assertThrows(
                NullPointerException.class,
                () -> property.getEndpoint(null, null)
        );
    }
}