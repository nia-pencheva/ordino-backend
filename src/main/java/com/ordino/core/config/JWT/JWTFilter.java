package com.ordino.core.config.JWT;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordino.core.exception.ErrorResponse;
import com.ordino.core.exception.auth.InvalidJWTTokenException;
import com.ordino.core.exception.auth.JWTTokenExpiredException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private JWTService jwtService;
    private UserDetailsService userDetailsService;
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);

            if (token != null && !token.isEmpty()) {
                String username = jwtService.extractUsername(token);

                if(username == null) 
                    throw new InvalidJWTTokenException();
        
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    if(!username.equals(userDetails.getUsername())) 
                        throw new InvalidJWTTokenException();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JWTTokenExpiredException e) {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "TOKEN_EXPIRED");
            return;
        } catch (InvalidJWTTokenException e) {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "INVALID_TOKEN");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendJsonError(HttpServletResponse response, int status, String error) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), new ErrorResponse(error));
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
