package com.ordino.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordino.core.exception.auth.InvalidRefreshTokenException;
import com.ordino.core.exception.auth.RefreshTokenExpiredException;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private ObjectMapper objectMapper;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(objectMapper.writeValueAsString(new ErrorResponse("INVALID_CREDENTIALS")));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<String> handleInvalidRefreshToken(InvalidRefreshTokenException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(objectMapper.writeValueAsString(new ErrorResponse("INVALID_REFRESH_TOKEN")));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<String> handleRefreshTokenExpired(RefreshTokenExpiredException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(objectMapper.writeValueAsString(new ErrorResponse("REFRESH_TOKEN_EXPIRED")));
    }
}
