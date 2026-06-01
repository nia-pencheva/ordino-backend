package com.ordino.domain.auth.validation;

import com.ordino.domain.auth.model.dto.ChangePasswordRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, ChangePasswordRequestDTO> {

    @Override
    public boolean isValid(ChangePasswordRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getNewPassword() == null || dto.getConfirmPassword() == null) {
            return true;
        }
        if (dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
               .addPropertyNode("confirmPassword")
               .addConstraintViolation();
        return false;
    }
}
