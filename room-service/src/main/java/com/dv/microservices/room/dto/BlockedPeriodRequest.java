package com.dv.microservices.room.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record BlockedPeriodRequest(
    @NotNull(message = "Start blocked date cannot be null")
    @JsonProperty("start_blocked")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startBlocked,

    @NotNull(message = "End blocked date cannot be null")
    @JsonProperty("end_blocked")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endBlocked) {
        
}
