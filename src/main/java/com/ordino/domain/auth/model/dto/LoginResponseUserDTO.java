package com.ordino.domain.auth.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseUserDTO {
    private Long id;
    private String username;
    private String name;
    private List<String> roles;
}
