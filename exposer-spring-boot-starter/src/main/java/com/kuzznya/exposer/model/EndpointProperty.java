package com.kuzznya.exposer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Data
@NoArgsConstructor
public class EndpointProperty {
    @NonNull
    private RequestMethod httpMethod;

    private String bean;
    @NonNull
    private String beanMethod;

    private Map<String, String> params;

    public Endpoint getEndpoint(@NonNull String path, @NonNull String beanName) {
        return new Endpoint(path, httpMethod, bean != null ? bean : beanName, beanMethod, params);
    }
}
