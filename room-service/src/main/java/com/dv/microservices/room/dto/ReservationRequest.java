package com.dv.microservices.room.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(

        @NotNull(message = "RESERVATION ID cannot be null") String id,
        
        @JsonProperty("user_id") @NotNull(message = "User ID cannot be null") Integer userId,

        @NotNull(message = "ROOM ID cannot be null") @JsonProperty("room_id") Integer roomId,

        @NotNull(message = "PRICE cannot be null") Float price,

        @JsonProperty("start_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") @NotNull(message = "Start date cannot be null") @FutureOrPresent(message = "Start date must be today or a future date") LocalDate startDate,

        @JsonProperty("end_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") @NotNull(message = "End date cannot be null") @Future(message = "End date must be future date") LocalDate endDate,

        @NotNull(message = "Status cannot be null") Boolean status,

        @JsonProperty("reservation_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") @NotNull(message = "Reservation date cannot be null") @FutureOrPresent(message = "Reservation date must be today or a future date") LocalDate reservationDate,

        @JsonProperty("payment_status") @NotNull(message = "Payment status cannot be null") Boolean paymentStatus,

        @JsonProperty("cancellation_reason") String cancellationReason) {

    public ReservationRequest withUpdatedRoomAndPrice(Integer roomId, Float price) {
        return new ReservationRequest(
                this.id,
                this.userId,
                roomId,
                price,
                this.startDate,
                this.endDate,
                this.status,
                this.reservationDate,
                this.paymentStatus,
                this.cancellationReason);
    }

}
