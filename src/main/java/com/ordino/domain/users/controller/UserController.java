package com.ordino.domain.users.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.users.model.dto.UserResponseDTO;
import com.ordino.domain.users.service.UserService;

import lombok.AllArgsConstructor;

import java.util.List;


@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseDTO> users() {
        return userService.users();
    }
}
