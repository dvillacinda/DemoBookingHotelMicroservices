package com.dv.microservices.reservation.dto;


import java.time.LocalDate;
import java.util.UUID;

import com.dv.microservices.reservation.model.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
    
    String id,
    
    @JsonProperty("user_id")
    Integer userId,

    @JsonProperty("room_id")
    Integer roomId,

    Float price,

    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be today or a future date")
    LocalDate startDate,

    @JsonProperty("end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be future date")
    LocalDate endDate,


    @NotNull(message = "Status cannot be null")
    Boolean status,

    @JsonProperty("reservation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Reservation date cannot be null")
    @FutureOrPresent(message = "Reservation date must be today or a future date")
    LocalDate reservationDate,

    @JsonProperty("payment_status")
    @NotNull(message = "Payment status cannot be null")
    Boolean paymentStatus,

    @JsonProperty("cancellation_reason")
    String cancellationReason) {

    public Reservation toReservation() {
        String reservationId = (id() == null || id().isBlank()) ? UUID.randomUUID().toString() : id();
        return new Reservation(
            reservationId,
            userId,
            roomId,
            startDate,
            endDate,
            price,
            status,
            reservationDate,
            paymentStatus,
            cancellationReason
        );
    }
}
