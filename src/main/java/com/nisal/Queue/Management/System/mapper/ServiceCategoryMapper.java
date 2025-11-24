package com.nisal.Queue.Management.System.mapper;

import com.nisal.Queue.Management.System.dto.ServiceCategoryDTO;
import com.nisal.Queue.Management.System.entity.ServiceCategory;

public class ServiceCategoryMapper {
    public static ServiceCategoryDTO toDto(ServiceCategory serviceCategory) {
        return ServiceCategoryDTO.builder()
                .id(serviceCategory.getId())
                .name(serviceCategory.getName())
                .description(serviceCategory.getDescription())
                .catImageUrl(serviceCategory.getCatImageUrl())
                .build();
    }

    public static ServiceCategory toEntity(ServiceCategoryDTO serviceCategoryDTO) {
        return ServiceCategory.builder()
                .id(serviceCategoryDTO.getId())
                .name(serviceCategoryDTO.getName())
                .description(serviceCategoryDTO.getDescription())
                .catImageUrl(serviceCategoryDTO.getCatImageUrl())
                .build();
    }
}
