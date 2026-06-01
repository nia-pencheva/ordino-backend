package com.ordino.core.exception.auth;

public class JWTTokenExpiredException extends RuntimeException {
    public JWTTokenExpiredException() {
        super("JWT token has expired");
    }
}
