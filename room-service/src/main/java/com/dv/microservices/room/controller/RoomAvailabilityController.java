package com.dv.microservices.room.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dv.microservices.room.dto.RoomAvailabilityRequest;
import com.dv.microservices.room.service.RoomAvailabilityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/room")
@RequiredArgsConstructor
public class RoomAvailabilityController {
    private final RoomAvailabilityService roomAvailabilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeReservation(@Valid @RequestBody RoomAvailabilityRequest roomAvailabilityRequest){
        roomAvailabilityService.saveRoomAvailability(roomAvailabilityRequest);
        return "Room Availability Placed Successfully!";
    }
    
}
