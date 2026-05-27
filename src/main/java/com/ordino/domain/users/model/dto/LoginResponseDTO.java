package com.ordino.domain.users.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private LoginResponseUserDTO user;
    private String token;
}
