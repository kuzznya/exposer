package com.github.kuzznya.exposer.core.util;

public class EvaluationException extends RuntimeException {
    public EvaluationException() {
        super("Cannot evaluate the expression");
    }

    public EvaluationException(String message) {
        super(message);
    }

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
