package com.dv.microservices.room.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.dv.microservices.room.dto.RoomSelectionRequest;
import com.dv.microservices.room.model.RoomAvailability;
import com.dv.microservices.room.service.RoomAvailabilityService;
import com.dv.microservices.room.service.RoomSelectionManager;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/room")
@RequiredArgsConstructor
public class RoomAvailabilityController {
    private final RoomAvailabilityService roomAService;
    private final RoomSelectionManager roomSelectionManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeRoom(@Valid @RequestBody RoomAvailabilityRequest roomAvailabilityRequest) {
        roomAService.saveRoomAvailability(roomAvailabilityRequest);
        return "Room Availability Placed Successfully!";
    }

    @PutMapping("/process-room-selection")
    @ResponseStatus(HttpStatus.OK)
    public ReservationRequest processRoomSelection(@RequestBody ReservationRequest request) {
        List<RoomAvailability> availableRooms = roomAService.selectAvailableRooms(request);
        if (availableRooms.isEmpty()) {
            return null;
        }

        RoomAvailability selectedRoom = roomSelectionManager.promptUserForRoomSelectionWithTimeout(
                availableRooms,
                request.id(),
                30 // Timeout 30 sec
        );

        if (selectedRoom == null) {
            return null;
        }

        roomAService.updateRoomWithReservationParams(request.id(), request.startDate(), request.endDate(),
                request.roomId());

        return request.withUpdatedRoomAndPrice(
                selectedRoom.getRoomId(),
                roomAService.getPrice(selectedRoom.getRoomId()));
    }

    @PostMapping("/user-select-room")
    @ResponseStatus(HttpStatus.OK)
    public String userSelectRoom(@RequestBody RoomSelectionRequest selectionRequest) {
        boolean success = roomSelectionManager.handleUserSelection(
                selectionRequest.reservationId(),
                selectionRequest.roomId());

        return success ? "Room selection successful." : "Invalid selection or timeout reached.";
    }

    @GetMapping("/get-list")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomRequest> getAvailableRoomRequests(@RequestParam LocalDate starDate, @RequestParam LocalDate endDate) {
        
        return null;
    }
}
