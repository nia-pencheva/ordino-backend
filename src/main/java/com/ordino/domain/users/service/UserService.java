package com.ordino.domain.users.service;

import java.util.Set;

import com.ordino.domain.users.model.dto.AddUserResponseDTO;
import com.ordino.domain.users.model.dto.ResetPasswordResponseDTO;
import com.ordino.domain.users.model.dto.RoleResponseDTO;
import com.ordino.domain.users.model.dto.UserRequestDTO;
import com.ordino.domain.users.model.dto.UserResponseDTO;
import com.ordino.domain.users.model.dto.UsersPageResponseDTO;

import jakarta.persistence.EntityNotFoundException;

public interface UserService {
    UsersPageResponseDTO getUsers(String search, Integer page, Integer pageSize, Long roleId);

    Set<RoleResponseDTO> getRoles();

    UserResponseDTO getUser(Long id) throws EntityNotFoundException;

    AddUserResponseDTO addUser(UserRequestDTO dto);

    void saveUser(Long id, UserRequestDTO dto);

    void deleteUser(Long id);

    ResetPasswordResponseDTO resetPassword(Long id);
}
