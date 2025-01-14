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
import org.springframework.web.bind.annotation.RestController;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;
import com.dv.microservices.reservation.exceptions.NotFoundException;
import com.dv.microservices.reservation.service.CacheService;
import com.dv.microservices.reservation.service.ReservationOrchestrator;
import com.dv.microservices.reservation.service.ReservationService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/reservation")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationOrchestrator reservationOrchestrator;
    private final CacheService cacheService;

    @PostMapping("/init-reservation")
    public ResponseEntity<Map<String, Object>> initReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        String reservationId = UUID.randomUUID().toString();

        String reservationIdHash = ReservationService.createReservationIdHash(reservationId);

        reservationRequest = new ReservationRequest(
                reservationId,
                reservationRequest.userId(),
                reservationRequest.roomId(),
                reservationRequest.price(),
                reservationRequest.startDate(),
                reservationRequest.endDate(),
                reservationRequest.status(),
                reservationRequest.reservationDate(),
                reservationRequest.paymentStatus(),
                reservationRequest.cancellationReason());

        cacheService.storeReservationRequest(reservationId, reservationRequest);
        cacheService.storeReservationIdHash(reservationIdHash, reservationId);

        log.info("Reservation initialized with reservationId: {}", reservationId);

        Map<String, Object> response = new HashMap<>();
        response.put("reservationIdHash", reservationIdHash); 

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-available-rooms")
    public ResponseEntity<Map<String, Object>> getAvailableRooms(
            @RequestParam String startString,
            @RequestParam String endString) {

        LocalDate startDate = LocalDate.parse(startString);
        LocalDate endDate = LocalDate.parse(endString);

        List<RoomRequest> roomRequests = reservationOrchestrator.getAvailableRooms(startDate, endDate);


        Map<String, Object> response = new HashMap<>();
        response.put("rooms", roomRequests);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/select-room-by-roomId")
    public ResponseEntity<String> selectRoomByPosition(
            @RequestParam String reservationIdHash,
            @RequestParam int roomId,
            @RequestParam Float roomPrice) {

        try {
            String reservationId = cacheService.retrieveReservationIdHash(reservationIdHash);
            if (reservationId == null) {
                log.error("Reservation ID not found for hash: {}", reservationIdHash);
                throw new NotFoundException("Invalid reservation hash provided.");
            }

            log.info("Processing room selection for reservationId: {}", reservationId);

            String result = reservationOrchestrator.handleRoomSelection(reservationIdHash, roomId,roomPrice);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to reserve room. Please try again later.");
        }
    }

}
