package com.github.kuzznya.exposer.core.model;

import lombok.NonNull;
import lombok.Value;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<String> getRequestParams() {
        return paramsMapping.values()
                .stream()
                .filter(param -> param.startsWith("?"))
                .map(param -> param.substring(1)).collect(Collectors.toList());
    }
}
