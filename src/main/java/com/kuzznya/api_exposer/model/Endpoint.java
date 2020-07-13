package com.kuzznya.api_exposer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Endpoint {
    @NonNull
    private String path;
    @NonNull
    private RequestMethod httpMethod;
    @NonNull
    private String beanName;
    @NonNull
    private String beanMethod;

    private Map<String, String> paramsMapping;

    public List<String> getRequestParams() {
        return paramsMapping.values()
                .stream()
                .filter(param -> param.startsWith("?"))
                .map(param -> param.substring(1)).collect(Collectors.toList());
    }
}
