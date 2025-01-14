package com.dv.microservices.user.dto;

import com.dv.microservices.user.model.ReservationHistory;
import com.dv.microservices.user.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

import java.util.stream.Collectors;

public record UserRequest(
        @NotBlank(message = "name can't be blank") String name,

        @NotBlank(message = "email can't be blank") @Email(message = "email not valid") String email,

        @NotBlank(message = "phone number can't be blank") @JsonProperty("phone_number") String phoneNumber,

        @JsonProperty("reservation_history") @Valid List<ReservationHistoryRequest> reservationHistory) {
    public User toUser() {
        User user = new User();
        user.setEmail(this.email());
        user.setName(this.name());
        user.setPhoneNumber(this.phoneNumber());

        List<ReservationHistory> reservationHistories = null;
        if (reservationHistory != null && !reservationHistory.isEmpty()) {
            reservationHistories = reservationHistory.stream()
                    .map(rhRequest -> new ReservationHistory(rhRequest.reservationId(), user))
                    .collect(Collectors.toList());
        }
        user.setReservationHistories(reservationHistories);

        return user;
    }
}
