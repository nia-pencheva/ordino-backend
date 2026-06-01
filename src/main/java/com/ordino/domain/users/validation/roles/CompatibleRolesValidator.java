package com.ordino.domain.users.validation.roles;

import com.ordino.domain.users.repository.RoleRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompatibleRolesValidator implements ConstraintValidator<CompatibleRoles, List<String>> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean isValid(List<String> roles, ConstraintValidatorContext context) {
        if (roles == null || roles.size() < 2) {
            return true;
        }
        List<Object[]> incompatiblePairs = roleRepository.findIncompatiblePairs(roles);
        if (!incompatiblePairs.isEmpty()) {
            Object[] pair = incompatiblePairs.get(0);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("Roles '%s' and '%s' are incompatible", pair[0], pair[1])
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
