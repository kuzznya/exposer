package com.github.kuzznya.exposer.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRequestBodyClass {
    List<Object> list;
    String key1;
}
