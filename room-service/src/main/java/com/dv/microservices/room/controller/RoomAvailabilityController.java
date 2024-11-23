package com.dv.microservices.room.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.dv.microservices.room.service.RoomAvailabilityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/room")
@RequiredArgsConstructor
public class RoomAvailabilityController {
    private final RoomAvailabilityService roomAService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeRoom(@Valid @RequestBody RoomAvailabilityRequest roomAvailabilityRequest) {
        roomAService.saveRoomAvailability(roomAvailabilityRequest);
        return "Room Availability Placed Successfully!";
    }

    

    @GetMapping("/get-list")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomRequest> getAvailableRoomRequests(@RequestParam String starDate,
            @RequestParam String endDate) {

        return roomAService.selectAvailableRoomsRequests(LocalDate.parse(starDate), LocalDate.parse(endDate));
    }

    @PutMapping("/set-reservation-values")
    public ResponseEntity<String> setReservationValues(
            @RequestParam int roomId, @RequestBody ReservationRequest reservationRequest) {
        roomAService.setReservationValues(roomId, reservationRequest); 
        return ResponseEntity.ok().build(); 
    }
}
