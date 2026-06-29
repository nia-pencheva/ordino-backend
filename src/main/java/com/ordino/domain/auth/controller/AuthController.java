package com.ordino.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.auth.model.dto.ChangePasswordRequestDTO;
import com.ordino.domain.auth.model.dto.LoginRequestDTO;
import com.ordino.domain.auth.model.dto.LoginResponseDTO;
import com.ordino.domain.auth.model.dto.RefreshRequestDTO;
import com.ordino.domain.auth.model.dto.RefreshResponseDTO;
import com.ordino.domain.auth.service.AuthService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {
        return ResponseEntity.ok().body(authService.login(requestDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDTO> refresh(@RequestBody RefreshRequestDTO requestDTO) {
        return ResponseEntity.ok().body(authService.refresh(requestDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshRequestDTO requestDTO) {
        authService.logout(requestDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal DatabaseUserDetails userDetails, @Valid @RequestBody ChangePasswordRequestDTO requestDTO) {
        authService.changePassword(userDetails.getUser(), requestDTO);
        return ResponseEntity.noContent().build();
    }    
}
