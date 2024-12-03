package com.dv.microservices.room.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dv.microservices.room.dto.ReservationRequest;
import com.dv.microservices.room.dto.RoomAvailabilityRequest;
import com.dv.microservices.room.dto.RoomRequest;
import com.dv.microservices.room.service.RoomAvailabilityOrchestrator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/room")
@RequiredArgsConstructor
public class RoomAvailabilityController {
    private final RoomAvailabilityOrchestrator orchestrator; 

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeRoom(@Valid @RequestBody RoomAvailabilityRequest roomAvailabilityRequest) {
        orchestrator.placeRoom(roomAvailabilityRequest);
        return "Room Availability Placed Successfully!";
    }

    @GetMapping("/get-list")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomRequest> getAvailableRoomRequests(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        return orchestrator.getAvailableRooms(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @PutMapping("/set-reservation-values")
    @ResponseStatus(HttpStatus.OK)
    public void setReservationValues(
            @RequestParam("roomId") int roomId,
            @RequestBody ReservationRequest reservationRequest) {
        orchestrator.setReservationValues(roomId, reservationRequest);

    }
}
