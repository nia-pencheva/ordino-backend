package com.ordino.core.config.JWT;

import java.util.function.Function;

import com.ordino.core.exception.auth.InvalidJWTTokenException;
import com.ordino.core.exception.auth.JWTTokenExpiredException;

import io.jsonwebtoken.Claims;

public interface JWTService {
    String generateToken(String username);

    String extractUsername(String token) throws JWTTokenExpiredException, InvalidJWTTokenException;

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JWTTokenExpiredException, InvalidJWTTokenException;
}
