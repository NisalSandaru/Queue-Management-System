package com.nisal.Queue.Management.System.service;

import com.nisal.Queue.Management.System.config.JwtProvider;
import com.nisal.Queue.Management.System.dto.PasswordUpdateDTO;
import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.enums.UserRole;
import com.nisal.Queue.Management.System.enums.UserStatus;
import com.nisal.Queue.Management.System.exceptions.UserNotFoundException;
import com.nisal.Queue.Management.System.mapper.UserMapper;
import com.nisal.Queue.Management.System.repository.UserRepository;
import com.nisal.Queue.Management.System.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public UserEntity getUserFromJwtToken(String token) throws UserNotFoundException {

        String email = jwtProvider.getEmailFromToken(token);
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Invalid token");
        }
        return user;
    }

    public UserEntity getCurrentUser() throws UserNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    public UserEntity getUserByEmail(String email) throws UserNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    public UserEntity getUserById(Long id) throws UserNotFoundException{
        return userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("user not Found")
        );
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        return userEntityList.stream().map(UserMapper::toDTO).toList();
    }

    public UserDTO updateUser(UserEntity user, Long id) throws UserNotFoundException, AccessDeniedException {
        UserEntity currentUser = getCurrentUser(); // Logged-in user
        UserEntity userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Only admin can update any user
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new AccessDeniedException("Only admin can update user data");
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


    public ApiResponse updateUserPassword(Long id, PasswordUpdateDTO dto) throws AccessDeniedException, BadRequestException {

        UserEntity currentUser = getCurrentUser();

        // Staff/Customer can only update their own password
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN) && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You don't have permission to change this password");
        }

        UserEntity userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check old password only if the user is changing their own password
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN) || currentUser.getId().equals(id)) {
            if (!passwordEncoder.matches(dto.getOldPassword(), userToUpdate.getPassword())) {
                throw new BadRequestException("Old password is incorrect");
            }
        }

        userToUpdate.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(userToUpdate);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Password changed successfully");
        return apiResponse;
    }

    public List<UserDTO> getUsersByRole(UserRole role) throws AccessDeniedException {
        UserEntity currentUser = getCurrentUser();
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new AccessDeniedException("You don't have permission to change this role");
        }
        List<UserEntity> userList = userRepository.findByRole(role);
        return userList.stream().map(UserMapper::toDTO).toList();
    }

    public List<UserDTO> allStaff() throws AccessDeniedException {
        return getUsersByRole(UserRole.ROLE_STAFF);
    }
    public List<UserDTO> allAdmins() throws AccessDeniedException {
        return getUsersByRole(UserRole.ROLE_ADMIN);
    }
    public List<UserDTO> allCustomers() throws AccessDeniedException {
        return getUsersByRole(UserRole.ROLE_CUSTOMER);
    }

    public UserDTO toggleStatus(Long id) throws AccessDeniedException {
        UserEntity currentUser = getCurrentUser();

        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new AccessDeniedException("Only admin can change user status");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setStatus(user.getStatus() == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE);

        return UserMapper.toDTO(userRepository.save(user));
    }

}
