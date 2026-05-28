package com.ordino.domain.auth.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private LoginResponseUserDTO user;
    private String token;
    private String refreshToken;
}
