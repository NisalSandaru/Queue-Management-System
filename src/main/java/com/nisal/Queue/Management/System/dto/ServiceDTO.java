package com.nisal.Queue.Management.System.dto;

import com.nisal.Queue.Management.System.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceDTO {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal price;
    private ServiceStatus status;
}
