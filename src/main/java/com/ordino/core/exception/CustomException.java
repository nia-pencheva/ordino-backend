package com.ordino.core.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String[] arguments;

    public CustomException(String message, String... errorParameters) {
        super(message);
        this.arguments = errorParameters;
    }
}

