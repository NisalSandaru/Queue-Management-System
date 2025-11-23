package com.nisal.Queue.Management.System.controller;

import com.nisal.Queue.Management.System.dto.PasswordUpdateDTO;
import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.mapper.UserMapper;
import com.nisal.Queue.Management.System.response.ApiResponse;
import com.nisal.Queue.Management.System.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserEntity user) throws AccessDeniedException {
        return ResponseEntity.ok(userService.updateUser(user, id));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateDTO dto) throws AccessDeniedException, BadRequestException {
        return ResponseEntity.ok(userService.updateUserPassword(id, dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/allStaff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllStaff() throws AccessDeniedException {
        List<UserDTO> users = userService.allStaff();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/allAdmins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllAdmins() throws AccessDeniedException {
        List<UserDTO> users = userService.allAdmins();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/allCustomers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllSCus() throws AccessDeniedException {
        List<UserDTO> users = userService.allCustomers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(
            @RequestHeader("Authorization") String jwt
    ){
        UserEntity user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ){
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> toggleUserStatus(@PathVariable Long id) throws AccessDeniedException {
        UserDTO updatedUser = userService.toggleStatus(id);
        return ResponseEntity.ok(updatedUser);
    }


}
