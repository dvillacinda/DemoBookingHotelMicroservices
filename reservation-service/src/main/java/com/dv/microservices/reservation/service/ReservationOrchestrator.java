package com.dv.microservices.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.client.RoomClient;
import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;
import com.dv.microservices.reservation.exceptions.NotFoundException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationOrchestrator {
    private final RoomClient roomClient;
    private final ReservationService reservationService;
    private final CacheService cacheService;

    public List<RoomRequest> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        try {

            List<RoomRequest> availableRooms = roomClient.getListAvailableRoom(
                    startDate.toString(), endDate.toString());

            if (availableRooms == null || availableRooms.isEmpty()) {
                throw new RuntimeException("No available rooms found for the given dates.");
            }

            return availableRooms;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get available rooms: " + e.getMessage(), e);
        }
    }

    public String handleRoomSelection(HttpSession session, int position) {
        
        String listId = (String) session.getAttribute("listId");
        if (listId == null) {
            throw new NotFoundException("Room list not found for the current session.");
        }

        // load room list from cache
        List<RoomRequest> roomRequests = cacheService.retrieveRoomRequests(listId);
        if (roomRequests == null) {
            throw new NotFoundException("Room list not found in cache.");
        }

        // valid position
        if (position < 0 || position >= roomRequests.size()) {
            throw new IllegalArgumentException("Invalid room position.");
        }

        // select room
        RoomRequest selectedRoom = roomRequests.get(position);

        String reservationId = (String) session.getAttribute("reservationId");
        if (reservationId == null) {
            throw new NotFoundException("Reservation not found for the current session.");
        }

        // load reservation from cache
        ReservationRequest cachedReservationRequest = cacheService.retrieveReservationRequest(reservationId);
        if (cachedReservationRequest == null) {
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

        setRoomParamsToStorage(selectedRoom.roomId(), request);

        return "Reservation stored successfully.";
    }

    public void setRoomParamsToStorage(int roomId, ReservationRequest reservationRequest) {
        try {
            roomClient.setReservationParamsToStorage(roomId, reservationRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set room parameters to storage: " + e.getMessage(), e);
        }
    }

}
