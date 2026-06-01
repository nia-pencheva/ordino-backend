package com.ordino.core.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordino.core.exception.auth.InvalidRefreshTokenException;
import com.ordino.core.exception.auth.RefreshTokenExpiredException;
import com.ordino.core.exception.response.ForbiddenExceptionResponse;
import com.ordino.core.exception.response.UnauthorizedExceptionResponse;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private ObjectMapper objectMapper;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(401))
                .body(objectMapper.writeValueAsString(new UnauthorizedExceptionResponse("INVALID_CREDENTIALS")));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<String> handleInvalidRefreshToken(InvalidRefreshTokenException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(401))
                .body(objectMapper.writeValueAsString(new UnauthorizedExceptionResponse("INVALID_REFRESH_TOKEN")));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<String> handleRefreshTokenExpired(RefreshTokenExpiredException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(401))
                .body(objectMapper.writeValueAsString(new UnauthorizedExceptionResponse("REFRESH_TOKEN_EXPIRED")));
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<String> handleForbiddenOperation(ForbiddenOperationException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(403))
                .body(objectMapper.writeValueAsString(new ForbiddenExceptionResponse(e.getMessage())));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(404))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) throws JsonProcessingException {
        List<Map<String, String>> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of("field", error.getField(), "message", error.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(HttpStatusCode.valueOf(422))
                .body(objectMapper.writeValueAsString(Map.of("errors", errors)));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidation(ValidationException e) throws JsonProcessingException {
        List<Map<String, String>> errors = List.of(Map.of("field", e.getField(), "message", e.getMessage()));
        return ResponseEntity
                .status(HttpStatusCode.valueOf(422))
                .body(objectMapper.writeValueAsString(Map.of("errors", errors)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException e) throws JsonProcessingException {
        List<Map<String, String>> errors = e.getConstraintViolations().stream()
                .map(violation -> {
                    String path = violation.getPropertyPath().toString();
                    String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                    return Map.of("field", field, "message", violation.getMessage());
                })
                .toList();

        return ResponseEntity
                .status(HttpStatusCode.valueOf(422))
                .body(objectMapper.writeValueAsString(Map.of("errors", errors)));
    }
}
