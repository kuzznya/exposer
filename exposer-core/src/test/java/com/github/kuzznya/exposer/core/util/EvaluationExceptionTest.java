package com.github.kuzznya.exposer.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationExceptionTest {
    @Test
    public void createEvaluationException() {
        assertThrows(
                EvaluationException.class,
                () -> {throw new EvaluationException();}
        );

        assertThrows(
                EvaluationException.class,
                () -> {throw new EvaluationException("Evaluation error");}
        );

        assertThrows(
                EvaluationException.class,
                () -> {throw new EvaluationException("Evaluation error", new Throwable());}
        );
    }
}