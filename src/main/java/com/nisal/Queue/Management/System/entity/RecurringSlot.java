package com.nisal.Queue.Management.System.entity;

import com.nisal.Queue.Management.System.enums.DayOfWeekEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "recurring_slots", uniqueConstraints = {
        @UniqueConstraint(name = "uc_service_day_start_end", columnNames = {"service_id", "day_of_week", "start_time", "end_time"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringSlot extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 5, nullable = false)
    private DayOfWeekEnum dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // capacity for this recurring pattern (default 1)
    @Column(name = "capacity", nullable = false)
    private Integer capacity = 1;
}
