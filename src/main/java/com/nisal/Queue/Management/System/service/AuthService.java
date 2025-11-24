package com.nisal.Queue.Management.System.service;

import com.nisal.Queue.Management.System.config.JwtProvider;
import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.enums.UserRole;
import com.nisal.Queue.Management.System.enums.UserStatus;
import com.nisal.Queue.Management.System.exceptions.UserNotFoundException;
import com.nisal.Queue.Management.System.mapper.UserMapper;
import com.nisal.Queue.Management.System.repository.UserRepository;
import com.nisal.Queue.Management.System.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserImplementation customUserImplementation;

    public AuthResponse CusSignUp(UserDTO userDTO) throws BadRequestException {
        UserEntity userOp = userRepository.findByEmail(userDTO.getEmail());
        if (userOp != null){
            throw new UserNotFoundException("email already registered !");
        }

        if (userDTO.getFirstName() == null || userDTO.getFirstName().isEmpty())
            throw new BadRequestException("First name cant be empty");

        if (userDTO.getLastName() == null || userDTO.getLastName().isEmpty())
            throw new BadRequestException("Last name cant be empty");

        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty())
            throw new BadRequestException("Email cant be empty");

        UserEntity user = UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(UserRole.ROLE_CUSTOMER)
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

    // Reusable method
    private AuthResponse createUserWithRole(UserDTO userDTO, UserRole role) throws BadRequestException {

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new BadRequestException("Email already registered!");
        }

        UserEntity user = UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(role)
                .status(UserStatus.ACTIVE)
                .profileImageUrl(userDTO.getProfileImageUrl())
                .build();

        UserEntity savedUser = userRepository.save(user);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(user.getEmail(), userDTO.getPassword());

        String token = jwtProvider.generateToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(token);
        res.setMessage("Registered Successfully");
        res.setUser(UserMapper.toDTO(savedUser));

        return res;
    }

    // STAFF + ADMIN create STAFF
    public AuthResponse staffSignUp(UserDTO userDTO) throws BadRequestException {
        return createUserWithRole(userDTO, UserRole.ROLE_STAFF);
    }

    // ADMIN creates ADMIN
    public AuthResponse adminSignUp(UserDTO userDTO) throws BadRequestException {
        return createUserWithRole(userDTO, UserRole.ROLE_ADMIN);
    }


    public AuthResponse login(UserDTO userDto) throws UserNotFoundException, BadRequestException {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String role = authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        UserEntity user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserNotFoundException("email cant find !");
        }

        UserStatus status = user.getStatus();
        if (status == UserStatus.INACTIVE){
            throw new BadRequestException("inactive user !");
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login Successfully");
        authResponse.setUser(UserMapper.toDTO(user));

        return authResponse;
    }

    private Authentication authenticate(String email, String password) throws UserNotFoundException, BadRequestException {

        UserDetails userDetails = customUserImplementation.loadUserByUsername(email);

        if(userDetails == null){
            throw new UserNotFoundException("email id doesn't exist "+ email);
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadRequestException("password doesn't match");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
