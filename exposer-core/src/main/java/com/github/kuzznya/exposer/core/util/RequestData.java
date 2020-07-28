package com.github.kuzznya.exposer.core.util;

import lombok.Value;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Value
public class RequestData {
    MultiValueMap<String, String> params;
    Map<String, String> pathVars;
}
