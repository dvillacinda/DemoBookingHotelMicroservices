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

    public String handleRoomSelection(HttpSession session, int position) {
        log.info("Handling room selection at position {} for the current session", position);

        String listId = (String) session.getAttribute("listId");
        if (listId == null) {
            log.error("Room list not found in session. Cannot proceed.");
            throw new NotFoundException("Room list not found for the current session.");
        }

        // load room list from cache
        List<RoomRequest> roomRequests = cacheService.retrieveRoomRequests(listId);
        if (roomRequests == null) {
            log.error("Room list not found or empty in cache for listId {}", listId);
            throw new NotFoundException("Room list not found in cache.");
        }

        // valid position
        if (position < 0 || position >= roomRequests.size()) {
            log.warn("Invalid room position {}. Total available rooms: {}", position, roomRequests.size());
            throw new IllegalArgumentException("Invalid room position.");
        }
        // select room
        RoomRequest selectedRoom = roomRequests.get(position);
        log.info("Selected room: roomId={}, price={}", selectedRoom.roomId(), selectedRoom.price());

        String reservationId = (String) session.getAttribute("reservationId");
        if (reservationId == null) {
            log.error("Reservation ID not found in session. Cannot proceed.");
            throw new NotFoundException("Reservation not found for the current session.");
        }

        // load reservation from cache
        ReservationRequest cachedReservationRequest = cacheService.retrieveReservationRequest(reservationId);
        if (cachedReservationRequest == null) {
            log.error("Reservation data not found in cache for reservationId {}", reservationId);
            throw new NotFoundException("Reservation data not found in cache.");
        }

        ReservationRequest request = new ReservationRequest(
                reservationId,
                1,
                selectedRoom.roomId(),
                selectedRoom.price(),
                cachedReservationRequest.startDate(),
                cachedReservationRequest.endDate(),
                false,
                cachedReservationRequest.reservationDate(),
                false,
                null);

        reservationService.completeReservation(request);
        log.info("Reservation completed successfully for reservationId {}", reservationId);

        setRoomParamsToStorage(selectedRoom.roomId(), request);

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
