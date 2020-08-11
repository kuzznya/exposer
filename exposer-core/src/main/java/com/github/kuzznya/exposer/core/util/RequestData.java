package com.github.kuzznya.exposer.core.util;

import lombok.Value;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Value
public class RequestData<RequestBodyType> {
    MultiValueMap<String, String> params;
    Map<String, String> pathVars;
    RequestBodyType body;
    Map<String, Object> bodyData;
}
