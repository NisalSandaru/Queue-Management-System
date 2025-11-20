package com.nisal.Queue.Management.System.mapper;

import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.entity.UserEntity;

public class UserMapper {

    public static UserDTO toDTO(UserEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .activationToken(user.getActivationToken())
                .firstName(user.getFirstName())
                .status(user.getStatus())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .profileImageUrl(user.getProfileImageUrl())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static UserEntity toEntity(UserDTO dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .password(dto.getPassword())
                .activationToken(dto.getActivationToken())
                .profileImageUrl(dto.getProfileImageUrl())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .firstName(dto.getFirstName())
                .role(dto.getRole())
                .lastName(dto.getLastName())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
