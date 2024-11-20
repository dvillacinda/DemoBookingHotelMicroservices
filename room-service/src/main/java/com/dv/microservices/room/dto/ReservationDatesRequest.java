package com.dv.microservices.room.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ReservationDatesRequest(
    @NotNull(message = "Start reservation date cannot be null")
    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "Start reservation date should be future or present")
    LocalDateTime startDate,

    @NotNull(message = "End reservation date cannot be null")
    @JsonProperty("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "End reservation date should be future")
    LocalDateTime endDate) {
        
}
