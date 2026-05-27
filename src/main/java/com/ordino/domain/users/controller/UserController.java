package com.ordino.domain.users.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.users.model.dto.LoginRequestDTO;
import com.ordino.domain.users.model.dto.LoginResponseDTO;
import com.ordino.domain.users.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO requestDTO) {
        return userService.login(requestDTO);
    }
}
