package com.kuzznya.api_exposer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    private String path;
    private RequestMethod method;
    private String classMethod;
    private boolean secured = false;
}
