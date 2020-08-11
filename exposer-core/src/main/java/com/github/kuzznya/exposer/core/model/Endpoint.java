package com.github.kuzznya.exposer.core.model;

import lombok.NonNull;
import lombok.Value;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Value
public class Endpoint {
    @NonNull
    String path;
    @NonNull
    RequestMethod httpMethod;
    @NonNull
    String beanName;
    @NonNull
    String beanMethod;

    Map<String, String> paramsMapping;
}
