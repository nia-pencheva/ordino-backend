package com.ordino.core.config.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordino.core.exception.response.UnauthorizedExceptionResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JsonErrorWriter {
    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, int status, String error) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), new UnauthorizedExceptionResponse(error));
    }
}
