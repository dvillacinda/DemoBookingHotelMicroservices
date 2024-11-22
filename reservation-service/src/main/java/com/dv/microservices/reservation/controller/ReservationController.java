package com.dv.microservices.reservation.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;
import com.dv.microservices.reservation.service.CacheService;
import com.dv.microservices.reservation.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final CacheService cacheService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String initReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        reservationService.initReservation(reservationRequest);
        return "Reservation start";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String completeReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        reservationService.completeReservation(reservationRequest);
        return "Placed Reservation successfully";
    }

    @GetMapping("/get-available-rooms")
    public ResponseEntity<Map<String, Object>> getAvailableRooms(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        // generate list room
        List<RoomRequest> roomRequests = reservationService.getAvailableRoom(startDate, endDate);

        String listId = UUID.randomUUID().toString();

        cacheService.store(listId, roomRequests);

        // build the response
        Map<String, Object> response = new HashMap<>();
        response.put("listId", listId);
        response.put("rooms", roomRequests);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/select-room-by-position")
    public ResponseEntity<RoomRequest> selectRoomByPosition(
            @RequestParam String listId,
            @RequestParam int position) {

        // return the cache list
        List<RoomRequest> roomRequests = cacheService.retrieve(listId);

        if (roomRequests == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Valid position
        if (position < 0 || position >= roomRequests.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        RoomRequest selectedRoom = roomRequests.get(position);

        return ResponseEntity.ok(selectedRoom);
    }

}
