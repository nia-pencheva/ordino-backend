package com.ordino.domain.users.validation.username;

import com.ordino.domain.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isBlank()) {
            return true;
        }
        Long excludeId = extractIdFromRequest();
        if (excludeId != null) {
            return !userRepository.existsByUsernameAndIdNot(username, excludeId);
        }
        return !userRepository.existsByUsername(username);
    }

    @SuppressWarnings("unchecked")
    private Long extractIdFromRequest() {
        Map<String, String> pathVariables = (Map<String, String>)
                request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables == null) {
            return null;
        }
        String raw = pathVariables.get("id");
        if (raw == null) {
            return null;
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
