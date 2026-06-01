package com.ordino.domain.users.model.dto;

import java.util.List;

import com.ordino.core.validation.ValidEmail;
import com.ordino.core.validation.ValidPhoneNumber;
import com.ordino.domain.users.validation.ValidFullName;
import com.ordino.domain.users.validation.email.UniqueEmail;
import com.ordino.domain.users.validation.phone_number.UniquePhoneNumber;
import com.ordino.domain.users.validation.roles.ValidRoles;
import com.ordino.domain.users.validation.username.ValidUsername;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {
    @ValidUsername
    private String username;

    @ValidFullName
    private String fullName;

    @ValidEmail
    @UniqueEmail
    private String email;

    @ValidPhoneNumber
    @UniquePhoneNumber
    private String phoneNumber;

    @ValidRoles
    private List<String> roles;
}
