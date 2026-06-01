package com.ordino.core.exception.auth;

public class InvalidJWTTokenException extends RuntimeException {
    public InvalidJWTTokenException() {
        super("Invalid JWT token");
    }
}
