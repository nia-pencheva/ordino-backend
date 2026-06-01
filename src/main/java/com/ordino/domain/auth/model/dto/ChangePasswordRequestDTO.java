package com.ordino.domain.auth.model.dto;

import com.ordino.domain.auth.validation.PasswordsMatch;
import com.ordino.domain.auth.validation.ValidPassword;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordsMatch
public class ChangePasswordRequestDTO {
    @ValidPassword
    private String newPassword;
    private String confirmPassword;
}
