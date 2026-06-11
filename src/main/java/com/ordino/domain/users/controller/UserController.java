package com.ordino.domain.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.users.validation.roles.id.ExistingRoleId;
import com.ordino.domain.users.model.dto.AddUserResponseDTO;
import com.ordino.domain.users.model.dto.ResetPasswordResponseDTO;
import com.ordino.domain.users.model.dto.RoleResponseDTO;
import com.ordino.domain.users.model.dto.UserRequestDTO;
import com.ordino.domain.users.model.dto.UserResponseDTO;
import com.ordino.domain.users.model.dto.UsersPageResponseDTO;
import com.ordino.domain.users.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import java.util.Set;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UsersPageResponseDTO> getUsers(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize,
        @RequestParam(required = false) @Positive @ExistingRoleId Long roleId
    ) {
        return ResponseEntity.ok(userService.getUsers(search, page, pageSize, roleId));
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<RoleResponseDTO>> getRoles() {
        return ResponseEntity.ok(userService.getRoles());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> user(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping()
    public ResponseEntity<AddUserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok().body(userService.addUser(dto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> saveUser(@PathVariable @Positive Long id, @Valid @RequestBody UserRequestDTO dto) {
        userService.saveUser(id, dto);

        return ResponseEntity.ok().build();
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<ResetPasswordResponseDTO> resetPassword(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(userService.resetPassword(id));
    }
}
