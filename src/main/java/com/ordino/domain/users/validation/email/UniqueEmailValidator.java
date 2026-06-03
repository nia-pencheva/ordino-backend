package com.ordino.domain.users.validation.email;

import com.ordino.core.util.PathVariablesUtil;
import com.ordino.domain.users.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }
        Long excludeId = PathVariablesUtil.extractPathId(request);
        if (excludeId != null) {
            return !userRepository.existsByEmailAndIdNot(email, excludeId);
        }
        return !userRepository.existsByEmail(email);
    }
}
