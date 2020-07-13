package com.kuzznya.api_exposer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
