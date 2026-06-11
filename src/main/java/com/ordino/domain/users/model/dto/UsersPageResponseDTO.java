package com.ordino.domain.users.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersPageResponseDTO {
    private List<UserResponseDTO> users;
    private Long totalElements;
    private Integer totalPages;
}
