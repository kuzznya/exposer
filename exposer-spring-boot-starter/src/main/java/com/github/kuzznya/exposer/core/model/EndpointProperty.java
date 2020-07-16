package com.github.kuzznya.exposer.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointProperty {
    @NonNull
    private RequestMethod httpMethod;

    private String bean;
    @NonNull
    private String beanMethod;

    private Map<String, String> params;

    public Endpoint getEndpoint(@NonNull String path, String beanName) {
        return new Endpoint(path, httpMethod, Optional.ofNullable(bean).orElse(beanName), beanMethod, params);
    }
}
