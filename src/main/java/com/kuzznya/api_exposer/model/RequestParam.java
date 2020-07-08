package com.kuzznya.api_exposer.model;

import lombok.Data;

@Data
public class RequestParam {
    private String name;
    private Class<?> type = String.class;
}
