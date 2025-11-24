package com.nisal.Queue.Management.System.service;

import com.nisal.Queue.Management.System.dto.ServiceDTO;
import com.nisal.Queue.Management.System.dto.UserDTO;
import com.nisal.Queue.Management.System.entity.ServiceCategory;
import com.nisal.Queue.Management.System.entity.ServiceEntity;
import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.enums.ServiceStatus;
import com.nisal.Queue.Management.System.enums.UserRole;
import com.nisal.Queue.Management.System.enums.UserStatus;
import com.nisal.Queue.Management.System.exceptions.CategoryNotFoundException;
import com.nisal.Queue.Management.System.exceptions.ServiceNotFoundException;
import com.nisal.Queue.Management.System.exceptions.UserNotFoundException;
import com.nisal.Queue.Management.System.mapper.ServiceMapper;
import com.nisal.Queue.Management.System.mapper.UserMapper;
import com.nisal.Queue.Management.System.repository.ServiceCategoryRepository;
import com.nisal.Queue.Management.System.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final UserService userService;

    // ================= CREATE =================
    public ServiceDTO createService(ServiceDTO request)
            throws BadRequestException, AccessDeniedException {

        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN)
            throw new AccessDeniedException("Only admin can create serviceEntities");

        if (request.getName() == null || request.getName().isEmpty())
            throw new BadRequestException("Service name cannot be empty");

        if (request.getName().length() > 150)
            throw new BadRequestException("Service name cannot be too long");

        if (request.getPrice() == null)
            throw new BadRequestException("Price name cannot be empty");

        if (request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Price cannot be negative");
        }

        if (request.getDurationMinutes() == null) {
            throw new BadRequestException("Duration cannot be empty");
        }

        if (request.getDurationMinutes() <= 0) {
            throw new BadRequestException("Duration cannot be negative");
        }

        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        ServiceEntity serviceEntity = ServiceMapper.toEntity(request, category);
        serviceEntity.setStatus(ServiceStatus.ACTIVE);

        return ServiceMapper.toDto(serviceRepository.save(serviceEntity));

    }

    // ================= READ =================
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(ServiceMapper::toDto)
                .toList();
    }

    public ServiceDTO getServiceById(Long id) {
        ServiceEntity s = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));
        return ServiceMapper.toDto(s);
    }

    // ================= UPDATE =================
    public ServiceDTO updateService(Long id, ServiceDTO request)
            throws BadRequestException, AccessDeniedException {

        var currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN)
            throw new AccessDeniedException("Only admin can update services");

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        if (request.getName() != null) service.setName(request.getName());
        if (request.getDescription() != null) service.setDescription(request.getDescription());
        if (request.getDurationMinutes() != null) service.setDurationMinutes(request.getDurationMinutes());
        if (request.getPrice() != null) service.setPrice(request.getPrice());

        // Update category if provided
        if (request.getCategoryId() != null) {
            ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            service.setCategory(category);
        }

        return ServiceMapper.toDto(serviceRepository.save(service));
    }

    public ServiceDTO toggleServiceStatus(Long id) throws AccessDeniedException {

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        service.setStatus(service.getStatus() == ServiceStatus.ACTIVE ? ServiceStatus.DISABLED : ServiceStatus.ACTIVE);

        return ServiceMapper.toDto(serviceRepository.save(service));
    }

    // ================= DELETE =================
    public void deleteService(Long id) throws AccessDeniedException {
        var currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN)
            throw new AccessDeniedException("Only admin can delete services");

        ServiceEntity s = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        serviceRepository.delete(s);
    }

    public List<ServiceDTO> getServiceByCategoryId(Long id) {
        ServiceCategory category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category not found")
        );
        List<ServiceEntity> list = serviceRepository.findByCategory(category);
        return list.stream().map(ServiceMapper::toDto).toList();
    }

}
