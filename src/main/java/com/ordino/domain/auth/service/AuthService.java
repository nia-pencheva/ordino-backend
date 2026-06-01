package com.ordino.domain.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ordino.core.config.JWT.JWTService;
import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.auth.model.dto.ChangePasswordRequestDTO;
import com.ordino.domain.auth.model.dto.LoginRequestDTO;
import com.ordino.domain.auth.model.dto.LoginResponseDTO;
import com.ordino.domain.auth.model.dto.LoginResponseUserDTO;
import com.ordino.domain.auth.model.dto.RefreshRequestDTO;
import com.ordino.domain.auth.model.dto.RefreshResponseDTO;
import com.ordino.domain.users.model.entity.Role;
import com.ordino.domain.users.model.entity.User;
import com.ordino.core.exception.ValidationException;
import com.ordino.domain.users.repository.UserRepository;

import lombok.AllArgsConstructor;

import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                requestDTO.getUsername(), requestDTO.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = ((DatabaseUserDetails) authentication.getPrincipal()).getUser();

        LoginResponseUserDTO responseUserDTO = new LoginResponseUserDTO();
        responseUserDTO.setUsername(user.getUsername());
        responseUserDTO.setName(user.getFullName());
        responseUserDTO.setRoles(
            user.getRoles().stream()
                .map(Role::getRole)
                .toList()
        );

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken(jwtService.generateToken(requestDTO.getUsername()));
        responseDTO.setRefreshToken(refreshTokenService.createRefreshToken(user).getToken());
        responseDTO.setUser(responseUserDTO);
        responseDTO.setPasswordChangeRequired(user.getPasswordChangedAt() == null);

        return responseDTO;
    }

    public RefreshResponseDTO refresh(RefreshRequestDTO requestDTO) {
        User user = refreshTokenService.rotateToken(requestDTO.getRefreshToken());

        RefreshResponseDTO responseDTO = new RefreshResponseDTO();
        responseDTO.setToken(jwtService.generateToken(user.getUsername()));
        responseDTO.setRefreshToken(refreshTokenService.createRefreshToken(user).getToken());

        return responseDTO;
    }

    public void logout(RefreshRequestDTO requestDTO) {
        refreshTokenService.deleteByToken(requestDTO.getRefreshToken());
    }

    public void changePassword(User user, ChangePasswordRequestDTO requestDTO) {
        if (passwordEncoder.matches(requestDTO.getNewPassword(), user.getPassword())) {
            throw new ValidationException("newPassword", "New password must differ from the current password");
        }
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
    }
}
