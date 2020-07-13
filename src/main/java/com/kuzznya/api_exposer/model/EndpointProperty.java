package com.kuzznya.api_exposer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
@NoArgsConstructor
public class EndpointProperty {
    @NonNull
    private RequestMethod httpMethod;

    private String bean;
    @NonNull
    private String beanMethod;

    public Endpoint getEndpoint(@NonNull String path, @NonNull String beanName) {
        return new Endpoint(path, httpMethod, beanName, beanMethod);
    }
}
