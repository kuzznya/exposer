package com.github.kuzznya.exposer.core.util;

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

        evaluator = new ParameterEvaluator(params, pathVars);
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
    }

}