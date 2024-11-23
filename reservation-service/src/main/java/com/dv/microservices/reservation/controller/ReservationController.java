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
import com.dv.microservices.reservation.exceptions.NotFoundException;
import com.dv.microservices.reservation.service.CacheService;
import com.dv.microservices.reservation.service.ReservationOrchestrator;
import com.dv.microservices.reservation.service.ReservationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationOrchestrator reservationOrchestrator;
    private final CacheService cacheService;

    @GetMapping("/init-reservation")
    public ResponseEntity<String> initReservation(@Valid @RequestBody ReservationRequest reservationRequest,
            HttpSession session) {
        String reservationId = UUID.randomUUID().toString();
        reservationRequest = new ReservationRequest(
                reservationId,
                1,
                reservationRequest.roomId(),
                reservationRequest.price(),
                reservationRequest.startDate(),
                reservationRequest.endDate(),
                reservationRequest.status(),
                reservationRequest.reservationDate(),
                reservationRequest.paymentStatus(),
                reservationRequest.cancellationReason());
        cacheService.storeReservationRequest(reservationRequest.id(), reservationRequest);
        
        session.setAttribute("reservationId", reservationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String completeReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        reservationService.completeReservation(reservationRequest);
        return "Placed Reservation successfully";
    }

    @GetMapping("/get-available-rooms")
    public ResponseEntity<Map<String, Object>> getAvailableRooms(
            @RequestParam String startString,
            @RequestParam String endString,
            HttpSession session) {
        LocalDate startDate = LocalDate.parse(startString);
        LocalDate endDate = LocalDate.parse(endString); 
        // generate list room
        List<RoomRequest> roomRequests = reservationOrchestrator.getAvailableRooms(startDate, endDate);

        String listId = UUID.randomUUID().toString();
        cacheService.storeRoomRequest(listId, roomRequests);
        session.setAttribute("listId", listId);

        // build the response
        Map<String, Object> response = new HashMap<>();
        response.put("rooms", roomRequests);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/select-room-by-position")
    public ResponseEntity<String> selectRoomByPosition(
            HttpSession session,
            @RequestParam int position) {

                try {
                    // Delegar al orquestador el manejo del flujo completo
                    String result = reservationOrchestrator.handleRoomSelection(session, position);
                    return ResponseEntity.status(HttpStatus.CREATED).body(result);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                } catch (NotFoundException e) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reserve room. Please try again later.");
                }
    }

}
