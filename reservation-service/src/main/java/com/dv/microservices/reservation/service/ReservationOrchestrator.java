package com.dv.microservices.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.client.RoomClient;
import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;
import com.dv.microservices.reservation.exceptions.NotFoundException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationOrchestrator {
    private final RoomClient roomClient;
    private final ReservationService reservationService;
    private final CacheService cacheService;

    public List<RoomRequest> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        try {
            log.info("Fetching available rooms for the date range {} to {}", startDate, endDate);
            List<RoomRequest> availableRooms = roomClient.getListAvailableRoom(
                    startDate.toString(), endDate.toString());

            if (availableRooms == null || availableRooms.isEmpty()) {
                log.warn("No available rooms found for the date range {} to {}", startDate, endDate);
                throw new NotFoundException(
                        "No available rooms found for the date range " + startDate + " to " + endDate);
            } else {
                log.info("Found {} available rooms for the date range {} to {}",
                        availableRooms.size(), startDate, endDate);
            }

            return availableRooms;
        } catch (CallNotPermittedException e) {
            log.error("Service unavailable: Circuit breaker is open for fetching available rooms", e);
            throw new RuntimeException("Service unavailable: Circuit breaker is open", e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching available rooms: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while fetching available rooms", e);
        }
    }

    public String handleRoomSelection(String reservationIdHash, int roomId, Float roomPrice) {
        log.info("Handling room selection for reservationIdHash: {} at position {}", reservationIdHash, roomId);

        
        String reservationId = cacheService.retrieveReservationIdHash(reservationIdHash);
        if (reservationId == null) {
            log.error("Reservation ID not found for hash: {}", reservationIdHash);
            throw new NotFoundException("Invalid reservation hash provided.");
        }
        ReservationRequest cachedReservationRequest = cacheService.retrieveReservationRequest(reservationId);
        if (cachedReservationRequest == null) {
            log.error("Reservation data not found in cache for reservationId: {}", reservationId);
            throw new NotFoundException("Reservation data not found for the provided reservation ID.");
        }

        // Update Reservation
        ReservationRequest updatedReservationRequest = new ReservationRequest(
                reservationId,
                cachedReservationRequest.userId(),
                roomId,
                roomPrice,
                cachedReservationRequest.startDate(),
                cachedReservationRequest.endDate(),
                false,
                cachedReservationRequest.reservationDate(),
                false,
                null);

        reservationService.completeReservation(updatedReservationRequest);
        log.info("Reservation completed successfully for reservationId: {}", reservationId);

        setRoomParamsToStorage(roomId, updatedReservationRequest);

        return "Reservation stored successfully.";
    }

    public void setRoomParamsToStorage(int roomId, ReservationRequest reservationRequest) {
        log.info("Setting reservation parameters to storage for roomId {}", roomId);
        try {
            roomClient.setReservationParamsToStorage(roomId, reservationRequest);
            log.info("Reservation parameters stored successfully for roomId {}", roomId);
        } catch (CallNotPermittedException e) {
            log.error("Service unavailable: Circuit breaker is open for roomId {}", roomId, e);
            throw new RuntimeException("Service unavailable: Circuit breaker is open", e);
        } catch (Exception e) {
            log.error("Unexpected error while setting room parameters for roomId {}: {}",
                    roomId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error while setting room parameters", e);
        }
    }

}
