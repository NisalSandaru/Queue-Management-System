package com.nisal.Queue.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String catImageUrl;
}
