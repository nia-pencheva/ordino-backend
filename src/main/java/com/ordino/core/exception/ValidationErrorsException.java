package com.ordino.core.exception;

import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class ValidationErrorsException extends RuntimeException {
    private final List<Map<String, String>> errors;

    public ValidationErrorsException(List<Map<String, String>> errors) {
        super("Validation failed");
        this.errors = errors;
    }
}
