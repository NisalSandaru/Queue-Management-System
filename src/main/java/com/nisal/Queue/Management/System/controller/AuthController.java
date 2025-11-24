package com.nisal.Queue.Management.System.controller;

import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.exceptions.UserNotFoundException;
import com.nisal.Queue.Management.System.response.AuthResponse;
import com.nisal.Queue.Management.System.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> cusSignup
            (@RequestBody UserDTO userDTO) throws UserNotFoundException, BadRequestException {
        AuthResponse res = authService.CusSignUp(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // STAFF + ADMIN can create STAFF
    @PostMapping("/signup/staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> staffSignup(@RequestBody UserDTO userDTO) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.staffSignUp(userDTO));
    }

    // ONLY ADMIN can create ADMIN
    @PostMapping("/signup/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> adminSignup(@RequestBody UserDTO userDTO) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.adminSignUp(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody UserDTO userDto) throws UserNotFoundException, BadRequestException {
        return ResponseEntity.ok(
                authService.login(userDto)
        );
    }
}
