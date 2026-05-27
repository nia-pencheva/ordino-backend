package com.ordino.domain.users.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseUserDTO {
    private String name;
    private List<String> roles;
}
