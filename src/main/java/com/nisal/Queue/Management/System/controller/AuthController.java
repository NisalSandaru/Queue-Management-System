package com.nisal.Queue.Management.System.controller;

import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.exceptions.UserException;
import com.nisal.Queue.Management.System.response.AuthResponse;
import com.nisal.Queue.Management.System.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            (@RequestBody UserDTO userDTO)throws UserException{
        AuthResponse res = authService.CusSignUp(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
