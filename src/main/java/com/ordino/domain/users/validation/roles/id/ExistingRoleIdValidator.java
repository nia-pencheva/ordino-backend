package com.ordino.domain.users.validation.roles.id;

import com.ordino.domain.users.repository.RoleRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistingRoleIdValidator implements ConstraintValidator<ExistingRoleId, Long> {

    @Autowired
    private RoleRepository repository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return repository.existsById(id);
    }
}
