package com.nisal.Queue.Management.System.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "time_slots",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_service_slotdate_start", columnNames = {"service_id", "slot_date", "start_time"})
        },
        indexes = {
                @Index(name = "idx_timeslot_service_date", columnList = "service_id, slot_date"),
                @Index(name = "idx_timeslot_provider", columnList = "provider_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which serviceEntity this slot is for
    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity serviceEntity;

    // Optional provider assignment for slot (if fixed)
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private UserEntity provider;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // capacity and available slots (update availableCapacity in serviceEntity layer/DB triggers)
    @Column(name = "capacity", nullable = false)
    private Integer capacity = 1;

    @Column(name = "available_capacity", nullable = false)
    private Integer availableCapacity = 1;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "OPEN"; // OPEN / CLOSED / HOLIDAY

    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
}
