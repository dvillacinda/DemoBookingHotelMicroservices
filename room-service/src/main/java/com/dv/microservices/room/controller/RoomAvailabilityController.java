package com.dv.microservices.room.controller;

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
import com.dv.microservices.room.model.RoomAvailability;
import com.dv.microservices.room.service.RoomAvailabilityService;
import com.dv.microservices.room.service.UserSelectionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/room")
@RequiredArgsConstructor
public class RoomAvailabilityController {
    private final RoomAvailabilityService roomAvailabilityService;
    private final UserSelectionService userSelectionService; 

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeRoom(@Valid @RequestBody RoomAvailabilityRequest roomAvailabilityRequest) {
        roomAvailabilityService.saveRoomAvailability(roomAvailabilityRequest);
        return "Room Availability Placed Successfully!";
    }

    /*@GetMapping("/get-room-id")

    @ResponseStatus(HttpStatus.OK)
    public int getRoomID(@RequestParam String reservationId) {
        return roomAvailabilityService.getRoomID(reservationId);
    }

    @GetMapping("/get-price")

    @ResponseStatus(HttpStatus.OK)
    public float getPrice(@RequestParam int roomId) {
        return roomAvailabilityService.getPrice(roomId);
    }*/

    @PutMapping("/process-room-selection")
    @ResponseStatus(HttpStatus.OK)
    public ReservationRequest processRoomSelection(@RequestBody ReservationRequest request) {
        List<RoomAvailability> availableRooms = roomAvailabilityService.initSelectedRoom(request);
        if (availableRooms.isEmpty()) {
            return null;
        }
        RoomAvailability selectedRoom =  userSelectionService.promptUserForRoomSelection(availableRooms);
        if (selectedRoom == null) {
            return null;
        }

        return request.withUpdatedRoomAndPrice(
                selectedRoom.getRoomId(),
                roomAvailabilityService.getPrice(selectedRoom.getRoomId()));
    }
}
