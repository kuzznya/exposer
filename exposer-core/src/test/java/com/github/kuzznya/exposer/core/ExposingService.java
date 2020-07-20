package com.github.kuzznya.exposer.core;

import org.springframework.stereotype.Service;

@Service
public class ExposingService {
    public String deleteMethod(String value) {
    return value + " deleted";
}

    public String testMethod() {
        return "test";
    }
}
