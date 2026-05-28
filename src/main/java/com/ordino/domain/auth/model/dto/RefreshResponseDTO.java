package com.ordino.domain.auth.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshResponseDTO {
    private String token;
    private String refreshToken;
}
