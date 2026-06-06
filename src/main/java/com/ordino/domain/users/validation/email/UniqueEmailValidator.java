package com.ordino.domain.users.validation.email;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.users.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator extends UniquePropertyValidator<UniqueEmail> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return userRepository.existsByEmail(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return userRepository.existsByEmailAndIdNot(value, excludeId);
    }
}
