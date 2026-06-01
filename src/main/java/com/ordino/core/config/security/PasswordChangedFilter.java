package com.ordino.core.config.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordino.core.exception.response.ForbiddenExceptionResponse;
import com.ordino.domain.users.model.entity.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PasswordChangedFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/login") 
            || path.equals("/refresh")
            || path.equals("/change-password");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null) {
            User user = ((DatabaseUserDetails) authentication.getPrincipal()).getUser();

            if(user.getPasswordChangedAt() == null) {
                response.setStatus(403);
                objectMapper.writeValue(response.getWriter(), new ForbiddenExceptionResponse("PASSWORD_CHANGE_REQUIRED"));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
