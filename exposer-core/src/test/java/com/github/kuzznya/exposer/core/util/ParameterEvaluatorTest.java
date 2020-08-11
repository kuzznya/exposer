package com.github.kuzznya.exposer.core.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuzznya.exposer.core.TestRequestBodyClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParameterEvaluatorTest {

    private static ParameterEvaluator evaluator;

    @BeforeAll
    static void setUp() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("key1", "value1");
        params.add("key1", "value2");
        params.add("key2", "value3");
        Map<String, String> pathVars = Map.of("pathVar1", "val1", "pathVar2", "val2");
        TestRequestBodyClass body = new TestRequestBodyClass(List.of(1, 2, 3), "bodyVal");

        ObjectMapper mapper = new ObjectMapper();
        JavaType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        Map<String, Object> requestBodyData = mapper.convertValue(body, mapType);

        evaluator = new ParameterEvaluator();
        evaluator.setRequestData(params, pathVars, body, requestBodyData);
    }

    @Test
    public void evaluationTest() {
        assertEquals(
                List.of("value1", "value2"),
                evaluator.getValue("$(params['key1'])")
        );
        assertEquals(
                "val2",
                evaluator.getValue("$(pathVars['pathVar2'])")
        );

        assertEquals(
                "val2test",
                evaluator.getValue("$(pathVars['pathVar2'])test")
        );

        assertEquals(
                "val2te$t",
                evaluator.getValue("$(pathVars['pathVar2'])te\\$t")
        );

        assertEquals(
                "bodyVal",
                evaluator.getValue("$(bodyData['key1'])")
        );

        assertEquals(
                List.of(1, 2, 3),
                evaluator.getValue("$(body.list)")
        );
    }

}