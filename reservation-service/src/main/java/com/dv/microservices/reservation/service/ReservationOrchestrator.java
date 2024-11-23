package com.dv.microservices.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.client.RoomClient;
import com.dv.microservices.reservation.dto.RoomRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationOrchestrator {
    private final RoomClient roomClient;
    private final ReservationService reservationService;

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

}
