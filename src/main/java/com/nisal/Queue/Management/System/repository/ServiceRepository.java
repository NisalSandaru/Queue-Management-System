package com.nisal.Queue.Management.System.repository;

import com.nisal.Queue.Management.System.entity.ServiceCategory;
import com.nisal.Queue.Management.System.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByCategory(ServiceCategory category);
}
