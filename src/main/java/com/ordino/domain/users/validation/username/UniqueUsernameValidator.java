package com.ordino.domain.users.validation.username;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.users.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUsernameValidator extends UniquePropertyValidator<UniqueUsername> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return userRepository.existsByUsername(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return userRepository.existsByUsernameAndIdNot(value, excludeId);
    }
}
