package com.ordino.domain.auth.service;

import com.ordino.domain.auth.model.dto.ChangePasswordRequestDTO;
import com.ordino.domain.auth.model.dto.LoginRequestDTO;
import com.ordino.domain.auth.model.dto.LoginResponseDTO;
import com.ordino.domain.auth.model.dto.RefreshRequestDTO;
import com.ordino.domain.auth.model.dto.RefreshResponseDTO;
import com.ordino.domain.users.model.entity.User;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO requestDTO);

    RefreshResponseDTO refresh(RefreshRequestDTO requestDTO);

    void logout(RefreshRequestDTO requestDTO);

    void changePassword(User user, ChangePasswordRequestDTO requestDTO);
}
