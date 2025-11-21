package com.nisal.Queue.Management.System.service;

import com.nisal.Queue.Management.System.config.JwtProvider;
import com.nisal.Queue.Management.System.dto.PasswordUpdateDTO;
import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.enums.UserRole;
import com.nisal.Queue.Management.System.exceptions.UserException;
import com.nisal.Queue.Management.System.mapper.UserMapper;
import com.nisal.Queue.Management.System.repository.UserRepository;
import com.nisal.Queue.Management.System.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public UserEntity getUserFromJwtToken(String token) throws UserException {

        String email = jwtProvider.getEmailFromToken(token);
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("Invalid token");
        }
        return user;
    }

    public UserEntity getCurrentUser() throws UserException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found");
        }
        return user;
    }

    public UserEntity getUserByEmail(String email) throws UserException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found");
        }
        return user;
    }

    public UserEntity getUserById(Long id) throws UserException, Exception {
        return userRepository.findById(id).orElseThrow(
                ()-> new Exception("user not Found")
        );
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO updateUser(UserEntity user, Long id) throws UserException {
        UserEntity currentUser = getCurrentUser(); // Logged-in user
        UserEntity userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Only admin can update any user
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new UserException("Only admin can update user data");
        }

        // Admin can update all fields
        if (user.getFirstName() != null) userToUpdate.setFirstName(user.getFirstName());
        if (user.getLastName() != null) userToUpdate.setLastName(user.getLastName());
        if (user.getPhone() != null) userToUpdate.setPhone(user.getPhone());
        if (user.getProfileImageUrl() != null) userToUpdate.setProfileImageUrl(user.getProfileImageUrl());
        if (user.getRole() != null) userToUpdate.setRole(user.getRole());
        if (user.getStatus() != null) userToUpdate.setStatus(user.getStatus());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        UserEntity savesUser = userRepository.save(userToUpdate);
        return UserMapper.toDTO(savesUser);
    }


    public ApiResponse updateUserPassword(Long id, PasswordUpdateDTO dto) {

        UserEntity currentUser = getCurrentUser();

        // Staff/Customer can only update their own password
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN) && !currentUser.getId().equals(id)) {
            throw new UserException("You don't have permission to change this password");
        }

        UserEntity userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check old password only if the user is changing their own password
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN) || currentUser.getId().equals(id)) {
            if (!passwordEncoder.matches(dto.getOldPassword(), userToUpdate.getPassword())) {
                throw new UserException("Old password is incorrect");
            }
        }

        userToUpdate.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(userToUpdate);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Password changed successfully");
        return apiResponse;
    }

}
