package com.nisal.Queue.Management.System.controller;

import com.nisal.Queue.Management.System.dto.ServiceDTO;
import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.service.ServiceService;
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
@RequestMapping("/services")
public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDTO> addService(@RequestBody ServiceDTO serviceDTO) throws AccessDeniedException, BadRequestException {
        ServiceDTO dto = serviceService.createService(serviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAll() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDTO> update(@PathVariable Long id,
                                             @RequestBody ServiceDTO request)
            throws Exception {
        return ResponseEntity.ok(serviceService.updateService(id, request));
    }

    @PatchMapping("/{id}/toggle-service-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDTO> toggleUserStatus(@PathVariable Long id) throws AccessDeniedException {
        ServiceDTO updatedService = serviceService.toggleServiceStatus(id);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id)
            throws AccessDeniedException {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<List<ServiceDTO>> getByCategory(@PathVariable Long categoryId) {
        List<ServiceDTO> list = serviceService.getServiceByCategoryId(categoryId);
        return ResponseEntity.ok(list);
    }

}
