package com.dv.microservices.room.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.dv.microservices.room.model.BlockedPeriod;
import com.dv.microservices.room.model.ReservationDates;
import com.dv.microservices.room.model.RoomAvailability;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomAvailabilityRequest(
    @NotNull(message = "Room ID cannot be null")
    @Positive(message = "Room ID must be greater than zero")
    @JsonProperty("room_id")
    int roomId,


    @JsonProperty("blocked_period")
    @Valid List<BlockedPeriodRequest> blockedPeriod,
    
    @JsonProperty("reservation_dates")
    @Valid List<ReservationDatesRequest> rDatesRequests
    
    ) {

    public RoomAvailability toRoomAvailability() {
        // create and return the entity 
        RoomAvailability roomAvailability = new RoomAvailability();
        roomAvailability.setRoomId(this.roomId());

        // mapped Block Period Request to Block Period
        List<BlockedPeriod> blockedPeriods = null;
        if (blockedPeriod != null && !blockedPeriod.isEmpty()) {
            blockedPeriods = blockedPeriod.stream()
                .map(bpRequest -> new BlockedPeriod(bpRequest.startBlocked(), bpRequest.endBlocked(),roomAvailability))
                .collect(Collectors.toList());
        }
        // mapped reservation dates request to reservation dates
        List<ReservationDates> reservationDates = null;
        if (rDatesRequests != null && !rDatesRequests.isEmpty()) {
            reservationDates = rDatesRequests().stream()
                .map(rdRequest -> new ReservationDates(rdRequest.startDate(), rdRequest.endDate(),roomAvailability))
                .collect(Collectors.toList());
        }
        
        roomAvailability.setBlockedPeriod(blockedPeriods);
        roomAvailability.setReservationDates(reservationDates);
        return roomAvailability;
    }
}
