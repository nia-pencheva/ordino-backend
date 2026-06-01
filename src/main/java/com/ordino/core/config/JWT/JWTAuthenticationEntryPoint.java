package com.ordino.core.config.JWT;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final JsonErrorWriter jsonErrorWriter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        jsonErrorWriter.write(response, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
    }
}
