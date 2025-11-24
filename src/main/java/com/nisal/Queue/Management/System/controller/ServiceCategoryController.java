package com.nisal.Queue.Management.System.controller;

import com.nisal.Queue.Management.System.dto.ServiceCategoryDTO;
import com.nisal.Queue.Management.System.service.ServiceCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class ServiceCategoryController {
    private final ServiceCategoryService serviceCategoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceCategoryDTO> addServiceCategory(@RequestBody ServiceCategoryDTO serviceCategoryDTO) throws BadRequestException, AccessDeniedException {
        ServiceCategoryDTO dto = serviceCategoryService.createCategory(serviceCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<ServiceCategoryDTO>> getAllServiceCategories(){
        List<ServiceCategoryDTO> dto = serviceCategoryService.getAllCategory();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceCategoryDTO> updateServiceCategory(
            @RequestBody ServiceCategoryDTO serviceCategoryDTO,
            @PathVariable Long id) throws BadRequestException, AccessDeniedException {
        ServiceCategoryDTO dto = serviceCategoryService.updateCategory(serviceCategoryDTO, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) throws AccessDeniedException {
        serviceCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
