package com.nisal.Queue.Management.System.service;

import com.nisal.Queue.Management.System.dto.ServiceCategoryDTO;
import com.nisal.Queue.Management.System.entity.ServiceCategory;
import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.enums.UserRole;
import com.nisal.Queue.Management.System.exceptions.CategoryNotFoundException;
import com.nisal.Queue.Management.System.mapper.ServiceCategoryMapper;
import com.nisal.Queue.Management.System.repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCategoryService {

    private final ServiceCategoryRepository repository;
    private final UserService userService;

    public ServiceCategoryDTO createCategory(ServiceCategoryDTO request) throws BadRequestException, AccessDeniedException {

        UserEntity currentUser = userService.getCurrentUser();
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new AccessDeniedException("Only admin can update user data");
        }

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new BadRequestException("Category name cannot be empty");
        }

        // Check duplicate category name
        if (repository.findByNameIgnoreCase(request.getName()).isPresent()){
            throw new BadRequestException("Category name already exists");
        }

        ServiceCategory category = ServiceCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .catImageUrl(request.getCatImageUrl())
                .build();

        ServiceCategory savedCat = repository.save(category);

        return ServiceCategoryMapper.toDto(savedCat);
    }

    public List<ServiceCategoryDTO> getAllCategory() {
        List<ServiceCategory> list = repository.findAll();
        return list.stream().map(ServiceCategoryMapper::toDto).toList();
    }

    public ServiceCategoryDTO updateCategory(ServiceCategoryDTO request, Long id) throws AccessDeniedException, BadRequestException {
        UserEntity currentUser = userService.getCurrentUser();
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new AccessDeniedException("Only admin can update user data");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new BadRequestException("Category name cannot be empty");
        }

        ServiceCategory cat = repository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found")
        );

        if (repository.findByNameIgnoreCase(request.getName()).isPresent()){
            throw new BadRequestException("Category name already exists");
        }

        cat.setName(request.getName());
        cat.setDescription(request.getDescription());
        cat.setCatImageUrl(request.getCatImageUrl());
        ServiceCategory savedCat = repository.save(cat);

        return ServiceCategoryMapper.toDto(savedCat);
    }

    public void deleteCategory(Long id) throws AccessDeniedException {
        UserEntity currentUser = userService.getCurrentUser();
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new AccessDeniedException("Only admin can delete categories");
        }

        ServiceCategory category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        // Optional safety rule: Block deleting categories that contain serviceEntities
        if (!category.getServiceEntities().isEmpty()) {
            throw new RuntimeException("Cannot delete category that has serviceEntities");
        }

        repository.delete(category);
    }


}
