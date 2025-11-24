package com.nisal.Queue.Management.System.mapper;

import com.nisal.Queue.Management.System.dto.ServiceDTO;
import com.nisal.Queue.Management.System.entity.ServiceEntity;
import com.nisal.Queue.Management.System.entity.ServiceCategory;

public class ServiceMapper {
    public static ServiceDTO toDto(ServiceEntity serviceEntity) {
        return ServiceDTO.builder()
                .id(serviceEntity.getId())
                .categoryId(serviceEntity.getCategory().getId())
                .name(serviceEntity.getName())
                .description(serviceEntity.getDescription())
                .durationMinutes(serviceEntity.getDurationMinutes())
                .price(serviceEntity.getPrice())
                .status(serviceEntity.getStatus())
                .build();
    }

    public static ServiceEntity toEntity(ServiceDTO dto, ServiceCategory category) {
        return ServiceEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .category(category)
                .description(dto.getDescription())
                .durationMinutes(dto.getDurationMinutes())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .build();
    }
}
