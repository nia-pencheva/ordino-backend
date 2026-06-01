package com.ordino.domain.users.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Integer id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private List<String> roles;
}
