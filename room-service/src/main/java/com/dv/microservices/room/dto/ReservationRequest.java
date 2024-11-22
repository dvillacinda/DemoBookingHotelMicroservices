package com.dv.microservices.room.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ReservationRequest(
        @JsonProperty("id") String id,
        @JsonProperty("user_id") Integer userId,
        @JsonProperty("room_id") Integer roomId,
        @JsonProperty("price") Float price,
        @JsonProperty("start_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate startDate,
        @JsonProperty("end_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate endDate,
        @JsonProperty("status") Boolean status,
        @JsonProperty("reservation_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate reservationDate,
        @JsonProperty("payment_status") Boolean paymentStatus,
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
