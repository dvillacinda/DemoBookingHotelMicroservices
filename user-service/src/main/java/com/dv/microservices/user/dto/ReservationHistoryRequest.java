package com.dv.microservices.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record ReservationHistoryRequest(
    @NotNull(message = "reservation number can't be null")
    @JsonProperty("reservation_id")
    String reservationId
) {

}
