package com.ordino.domain.users.validation.roles;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ValidRolesValidator implements ConstraintValidator<ValidRoles, List<String>> {

    @Override
    public boolean isValid(List<String> roles, ConstraintValidatorContext context) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        return roles.stream().noneMatch(role -> role == null || role.isBlank());
    }
}
