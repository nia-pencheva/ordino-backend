package com.ordino.domain.users.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.auth.repository.RefreshTokenRepository;
import com.ordino.domain.users.model.dto.AddUserResponseDTO;
import com.ordino.domain.users.model.dto.ResetPasswordResponseDTO;
import com.ordino.domain.users.model.dto.RoleResponseDTO;
import com.ordino.domain.users.model.dto.UserRequestDTO;
import com.ordino.domain.users.model.dto.UserResponseDTO;
import com.ordino.domain.users.model.dto.UsersPageResponseDTO;
import com.ordino.domain.users.model.entity.Role;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.RoleRepository;
import com.ordino.domain.users.repository.UserRepository;

import com.ordino.core.exception.ForbiddenOperationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    private final PasswordService passwordService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    
    public UsersPageResponseDTO getUsers(String search, Integer page, Integer pageSize, Long roleId) {
        Integer pageNumber = page != null ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<User> usersPage = (search == null)
                                    ? repository.findAllWithRolesPaginated(roleId, pageRequest)
                                    : repository.searchUsers(search, roleId, pageRequest);

        UsersPageResponseDTO responseDTO = new UsersPageResponseDTO();

        responseDTO.setUsers(
            usersPage.stream() 
                        .map(user -> {
                            UserResponseDTO userResponseDTO = mapper.map(user, UserResponseDTO.class);

                            userResponseDTO.setRoles(
                                user.getRoles()
                                    .stream()
                                    .map(Role::getRole)
                                    .toList()  
                            );

                            return userResponseDTO;
                        })  
                        .toList()
        );
        
        responseDTO.setTotalElements(usersPage.getTotalElements());
        responseDTO.setTotalPages(usersPage.getTotalPages());

        return responseDTO;
    }

    public Set<RoleResponseDTO> getRoles() {
        return repository.findAllDistinctRoles()
                        .stream()
                        .map(role -> mapper.map(role, RoleResponseDTO.class))
                        .collect(Collectors.toSet());
    }

    public UserResponseDTO getUser(Long id) throws EntityNotFoundException {
        User user = repository.findByIdWithRoles(id)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserResponseDTO dto = mapper.map(user, UserResponseDTO.class);
        dto.setRoles(
            user.getRoles()
                .stream()
                .map(Role::getRole)
                .collect(Collectors.toList())
        );

        return dto;
    }

    public AddUserResponseDTO addUser(UserRequestDTO dto) {
        User user = mapper.map(dto, User.class);

        String initialPassword = passwordService.generate();

        user.setPassword(passwordEncoder.encode(initialPassword));
        user.setRoles(roleRepository.findByRoleIn(dto.getRoles()));
        repository.save(user);
        
        AddUserResponseDTO responseDTO = new AddUserResponseDTO();
        responseDTO.setInitialPassword(initialPassword);

        return responseDTO;
    }

    @Transactional
    public void saveUser(Long id, UserRequestDTO dto) {
        User user = repository.findByIdWithRoles(id)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ((DatabaseUserDetails) authentication.getPrincipal()).getUser();

        boolean isSelf = currentUser.equals(user);
        boolean currentlyAdmin = user.getRoles().stream().anyMatch(r -> r.getRole().equals("admin"));
        boolean removingAdmin = !dto.getRoles().contains("admin");

        if (isSelf && currentlyAdmin && removingAdmin) {
            throw new ForbiddenOperationException(List.of("Cannot remove own admin role"));
        }

        user.setRoles(roleRepository.findByRoleIn(dto.getRoles()));
        mapper.map(dto, user);

        repository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = repository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(((DatabaseUserDetails) authentication.getPrincipal()).getUser().equals(user)) {
            throw new ForbiddenOperationException(List.of("Cannot delete own account"));
        }

        user.setDeletedAt(Instant.now());

        refreshTokenRepository.deleteByUser(user);
        
        repository.save(user);
    }

    @Transactional
    public ResetPasswordResponseDTO resetPassword(Long id) {
        User user = repository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String initialPassword = passwordService.generate();

        user.setPassword(passwordEncoder.encode(initialPassword));
        user.setPasswordChangedAt(null);

        repository.save(user);

        ResetPasswordResponseDTO responseDTO = new ResetPasswordResponseDTO();
        responseDTO.setNewPassword(initialPassword);

        refreshTokenRepository.deleteByUser(user);

        return responseDTO;
    }
}
