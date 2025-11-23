package com.nisal.Queue.Management.System.entity;

import com.nisal.Queue.Management.System.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "bookings",
        indexes = {
                @Index(name = "idx_booking_user", columnList = "user_id"),
                @Index(name = "idx_booking_slot", columnList = "timeslot_id")
        },
        uniqueConstraints = {
                // Prevent exact duplicate bookings for same slot & user at same start time
                @UniqueConstraint(name = "uc_slot_user_start", columnNames = {"timeslot_id", "user_id", "start_time"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // customer
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // service
    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    // reference to actual timeslot (may be null for ad-hoc custom bookings)
    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    private TimeSlot timeSlot;

    // provider assigned to this booking (nullable if not assigned)
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private UserEntity provider;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "participants", nullable = false)
    private Integer participants = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    // price snapshot at booking time
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
