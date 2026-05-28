package com.ordino.domain.users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.ordino.domain.users.model.dto.UserResponseDTO;
import com.ordino.domain.users.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final ModelMapper mapper;
    
    public List<UserResponseDTO> users() {
        return repository.findAllWithRoles()
                            .stream()
                            .map(user -> mapper.map(user, UserResponseDTO.class))
                            .collect(Collectors.toList());
    }
}
