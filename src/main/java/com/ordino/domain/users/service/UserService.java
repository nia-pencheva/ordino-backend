package com.ordino.domain.users.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ordino.core.config.JWT.JWTService;
import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.users.model.dto.LoginRequestDTO;
import com.ordino.domain.users.model.dto.LoginResponseDTO;
import com.ordino.domain.users.model.dto.LoginResponseUserDTO;
import com.ordino.domain.users.model.entity.Role;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                requestDTO.getUsername(), requestDTO.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = ((DatabaseUserDetails) authentication.getPrincipal()).getUser();

        LoginResponseUserDTO responseUserDTO = new LoginResponseUserDTO();
        responseUserDTO.setName(user.getFullName());
        responseUserDTO.setRoles(
            user.getRoles().stream()
                .map(Role::getRole)
                .toList()
        );


        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken(jwtService.generateToken(requestDTO.getUsername()));
        responseDTO.setUser(responseUserDTO);

        return responseDTO;
    }
}
