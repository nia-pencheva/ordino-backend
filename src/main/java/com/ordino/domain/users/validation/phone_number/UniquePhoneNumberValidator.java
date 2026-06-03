package com.ordino.domain.users.validation.phone_number;

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
public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true;
        }
        Long excludeId = PathVariablesUtil.extractPathId(request);
        if (excludeId != null) {
            return !userRepository.existsByPhoneNumberAndIdNot(phoneNumber, excludeId);
        }
        return !userRepository.existsByPhoneNumber(phoneNumber);
    }
}
