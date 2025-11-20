package com.nisal.Queue.Management.System.service;

import com.nisal.Queue.Management.System.config.JwtProvider;
import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.enums.UserRole;
import com.nisal.Queue.Management.System.enums.UserStatus;
import com.nisal.Queue.Management.System.exceptions.UserException;
import com.nisal.Queue.Management.System.mapper.UserMapper;
import com.nisal.Queue.Management.System.repository.UserRepository;
import com.nisal.Queue.Management.System.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthResponse CusSignUp(UserDTO userDTO){
        Optional<UserEntity> userOp = userRepository.findByEmail(userDTO.getEmail());
        if (userOp.isPresent()){
            throw new UserException("email already registered !");
        }
        UserEntity user = UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(UserRole.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .profileImageUrl(userDTO.getProfileImageUrl())
                .build();

        UserEntity savedUser = userRepository.save(user);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Registered Successfully");
        authResponse.setUser(UserMapper.toDTO(savedUser));

        return authResponse;
    }

}
