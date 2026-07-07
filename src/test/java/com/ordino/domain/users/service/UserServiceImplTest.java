package com.ordino.domain.users.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.auth.repository.RefreshTokenRepository;
import com.ordino.domain.users.model.dto.AddUserResponseDTO;
import com.ordino.domain.users.model.dto.ResetPasswordResponseDTO;
import com.ordino.domain.users.model.dto.UserRequestDTO;
import com.ordino.domain.users.model.entity.Role;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.RoleRepository;
import com.ordino.domain.users.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final PasswordService passwordService = mock(PasswordService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
    private final ModelMapper mapper = new ModelMapper();

    private final UserServiceImpl service = new UserServiceImpl(
            userRepository, roleRepository, mapper, passwordService, passwordEncoder, refreshTokenRepository
    );

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new DatabaseUserDetails(user), null, List.of())
        );
    }

    private User userWithId(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Test
    void addUser_validData_savesUserWithEncodedPasswordAndAssignedRoles() {
        UserRequestDTO dto = new UserRequestDTO("new.user", "New User", "new.user@test.local", "+10000000001", List.of("chef"));

        Role chefRole = new Role();
        chefRole.setRole("chef");

        when(passwordService.generate()).thenReturn("Tempor4ryPass!");
        when(passwordEncoder.encode("Tempor4ryPass!")).thenReturn("ENCODED");
        when(roleRepository.findByRoleIn(List.of("chef"))).thenReturn(List.of(chefRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddUserResponseDTO response = service.addUser(dto);

        assertThat(response.getInitialPassword()).isEqualTo("Tempor4ryPass!");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("new.user");
        assertThat(saved.getPassword()).isEqualTo("ENCODED");
        assertThat(saved.getRoles()).containsExactly(chefRole);
    }

    @Test
    void resetPassword_existingUser_generatesNewPasswordAndDeletesExistingRefreshToken() {
        User user = userWithId(5L);
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(passwordService.generate()).thenReturn("BrandNewPass1!");
        when(passwordEncoder.encode("BrandNewPass1!")).thenReturn("ENCODED_NEW");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResetPasswordResponseDTO response = service.resetPassword(5L);

        assertThat(response.getNewPassword()).isEqualTo("BrandNewPass1!");
        assertThat(user.getPassword()).isEqualTo("ENCODED_NEW");
        assertThat(user.getPasswordChangedAt()).isNull();
        verify(refreshTokenRepository).deleteByUser(user);
    }

    @Test
    void deleteUser_existingUser_deletesUserAndCascadesRefreshTokenDeletion() {
        User currentAdmin = userWithId(1L);
        User targetUser = userWithId(2L);

        authenticateAs(currentAdmin);
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));

        service.deleteUser(2L);

        assertThat(targetUser.getDeletedAt()).isNotNull();
        verify(refreshTokenRepository).deleteByUser(targetUser);
        verify(userRepository).save(targetUser);
    }

    @Test
    void deleteUser_ownAccount_throwsForbiddenOperationException() {
        User currentAdmin = userWithId(1L);

        authenticateAs(currentAdmin);
        when(userRepository.findById(1L)).thenReturn(Optional.of(currentAdmin));

        assertThatThrownBy(() -> service.deleteUser(1L))
                .isInstanceOf(ForbiddenOperationException.class);
    }
}
