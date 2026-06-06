package com.ordino.domain.users.validation.phone_number;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.users.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniquePhoneNumberValidator extends UniquePropertyValidator<UniquePhoneNumber> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return userRepository.existsByPhoneNumber(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return userRepository.existsByPhoneNumberAndIdNot(value, excludeId);
    }
}
