package com.nisal.Queue.Management.System.controller;

import com.nisal.Queue.Management.System.dto.PasswordUpdateDTO;
import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.mapper.UserMapper;
import com.nisal.Queue.Management.System.response.ApiResponse;
import com.nisal.Queue.Management.System.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.updateUser(user, id));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateDTO dto) {
        return ResponseEntity.ok(userService.updateUserPassword(id, dto));
    }

}
