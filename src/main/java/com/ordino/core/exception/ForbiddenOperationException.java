package com.ordino.core.exception;

public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String error) {
        super(error);
    }
}
