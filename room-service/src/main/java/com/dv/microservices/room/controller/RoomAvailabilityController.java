package com.dv.microservices.room.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/get-room-id")
    @ResponseStatus(HttpStatus.OK)
    public int getRoomID(@RequestParam String reservationId){
        return roomAvailabilityService.getRoomID(reservationId);
    }

    @GetMapping("/get-price")
    @ResponseStatus(HttpStatus.OK)
    public float getPrice(@RequestParam int roomId){
        return roomAvailabilityService.getPrice(roomId);
    }
    
}
