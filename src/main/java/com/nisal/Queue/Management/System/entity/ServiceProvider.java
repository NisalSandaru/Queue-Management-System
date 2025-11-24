package com.nisal.Queue.Management.System.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_providers", uniqueConstraints = {
        @UniqueConstraint(name = "uc_staff_service", columnNames = {"staff_id", "service_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProvider extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "staff_id", nullable = false)
    private UserEntity staff; // must be STAFF or ADMIN (enforce in serviceEntity layer)

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity serviceEntity;
}
