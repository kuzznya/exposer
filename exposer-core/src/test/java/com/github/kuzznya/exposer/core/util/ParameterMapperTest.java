package com.github.kuzznya.exposer.core.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterMapperTest {
    @SuppressWarnings("unused")
    public void testMethod(Integer param1, String param2, Set<String> param3, List<String> param4) {}

    private final List<MethodParameter> parameters = new LinkedList<>();
    private final ParameterEvaluator evaluator = new ParameterEvaluator();

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        Method method = ParameterMapperTest.class.getDeclaredMethod("testMethod",
                Integer.class, String.class, Set.class, List.class);

        parameters.clear();

        ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

        for (int i = 0; i< method.getParameters().length; i++) {
            MethodParameter parameter = new MethodParameter(method, i);
            parameter.initParameterNameDiscovery(nameDiscoverer);
            parameters.add(parameter);
        }

        evaluator.setRequestData(
                 new LinkedMultiValueMap<>(Map.of(
                        "rp1", List.of("rp1v1", "rp1v2"),
                        "rp2", List.of("rp2v1")
                )), Map.of("pv1", "pv1v", "pv2", "pv2v"),
                Map.of("bv1", "bv1v", "bv2", List.of("bv2v1", "bv2v2")),
                Map.of("bv1", "bv1v", "bv2", List.of("bv2v1", "bv2v2"))
        );

    }

    @Test
    void mapCollectionResult() {
        assertEquals(
                1,
                ParameterMapper.mapResult(parameters.get(0), List.of(1, 2, 3))
        );

        assertEquals(
                "str1",
                ParameterMapper.mapResult(parameters.get(1), List.of("str1", "str2"))
        );

        assertEquals(
                Set.of("str1", "str2"),
                ParameterMapper.mapResult(parameters.get(2), List.of("str1", "str2"))
        );

        assertEquals(
                List.of("str1", "str2"),
                ParameterMapper.mapResult(parameters.get(3), List.of("str1", "str2"))
        );
    }

    @Test
    void mapSingleResult() {
        assertEquals(
                1,
                ParameterMapper.mapResult(parameters.get(0), 1)
        );

        assertEquals(
                "str1",
                ParameterMapper.mapResult(parameters.get(1), "str1")
        );

        assertEquals(
                Set.of("str1"),
                ParameterMapper.mapResult(parameters.get(2), "str1")
        );

        assertEquals(
                List.of("str1"),
                ParameterMapper.mapResult(parameters.get(3), "str1")
        );
    }

    @Test
    void mapParams() {
        Map<String, String> mapping = Map.of(
                "param1",  "1",
                "param2", "params['rp1']",
                "param3", "pathVars['pv1']",
                "param4", "bodyData['bv2']"
        );

        assertEquals(
                List.of(1, "rp1v1", Set.of("pv1v"), List.of("bv2v1", "bv2v2")),
                ParameterMapper.mapParams(evaluator, parameters, mapping)
        );
    }
}