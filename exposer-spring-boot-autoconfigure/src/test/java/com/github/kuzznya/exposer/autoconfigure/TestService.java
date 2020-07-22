package com.github.kuzznya.exposer.autoconfigure;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    private String value = "test";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
