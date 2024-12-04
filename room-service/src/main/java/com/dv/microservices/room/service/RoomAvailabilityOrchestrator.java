package com.dv.microservices.room.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dv.microservices.room.client.InformationClient;
import com.dv.microservices.room.dto.ReservationRequest;
import com.dv.microservices.room.dto.RoomAvailabilityRequest;
import com.dv.microservices.room.dto.RoomRequest;
import com.dv.microservices.room.exceptions.NotFoundException;
import com.dv.microservices.room.model.RoomAvailability;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomAvailabilityOrchestrator {
    public final InformationClient informationClient;
    private final RoomAvailabilityService roomAvailabilityService;

    public float getRoomPrice(int roomId) {
        log.info("Fetching price for roomId: {}", roomId);
        return informationClient.getPrice(roomId);
    }

    public String getServicesInclude(int roomId) {
        log.info("Fetching services included for roomId: {}", roomId);
        return informationClient.getServicesInclude(roomId);
    }

    public int getRoomCapacity(int roomId) {
        log.info("Fetching capacity for roomId: {}", roomId);
        return informationClient.getCapacity(roomId);
    }

    public String getRoomDescription(int roomId) {
        log.info("Fetching description for roomId: {}", roomId);
        return informationClient.getDescription(roomId);
    }

    @Transactional
    public void placeRoom(RoomAvailabilityRequest request) {
        try {
            log.info("Placing room availability: {}", request);
            roomAvailabilityService.saveRoomAvailability(request);
            log.info("Room availability placed successfully.");
        } catch (Exception e) {
            log.error("Failed to place room availability. Reason: {}", e.getMessage());
            throw new RuntimeException("Unable to place room availability.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RoomRequest> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching available rooms for date range: {} to {}", startDate, endDate);
        List<RoomAvailability> availableRooms = roomAvailabilityService.getAvailableRooms(startDate, endDate);

        if (availableRooms.isEmpty()) {
            log.warn("No available rooms found for the specified date range. {} -> {}", startDate, endDate);
            throw new NotFoundException("No available rooms found for the specified date range" + startDate.toString()
                    + " -> " + endDate.toString());
        }

        log.info("Found {} available rooms. Fetching additional information from InformationClient.",
                availableRooms.size());
        return availableRooms.stream()
                .map(room -> {
                    try {
                        float price = getRoomPrice(room.getRoomId());
                        String description = getRoomDescription(room.getRoomId());
                        String servicesInclude = getServicesInclude(room.getRoomId());
                        int capacity = getRoomCapacity(room.getRoomId());

                        if (price == -1) {
                            log.warn("Failed to fetch price for roomId {}", room.getRoomId());
                            throw new RuntimeException("Failed to fetch price for roomId " + room.getRoomId());
                        }
                        if ("Description not available".equals(description)) {
                            log.warn("Failed to fetch description for roomId {}", room.getRoomId());
                            throw new RuntimeException("Failed to fetch description for roomId " + room.getRoomId());
                        }
                        if ("Services information not available".equals(servicesInclude)) {
                            log.warn("FFailed to fetch services for roomId {}", room.getRoomId());
                            throw new RuntimeException("Failed to fetch services for roomId " + room.getRoomId());
                        }
                        if (capacity == -1) {
                            log.warn("Failed to fetch capacity for roomId {}", room.getRoomId());
                            throw new RuntimeException("Failed to fetch capacity for roomId " + room.getRoomId());
                        }

                        return new RoomRequest(room.getRoomId(), description, price, capacity, servicesInclude);
                    } catch (Exception e) {
                        log.error("Failed to fetch information for roomId {}. Reason: {}", room.getRoomId(),
                                e.getMessage());
                        throw new RuntimeException("Failed to fetch information for roomId " + room.getRoomId(),e);
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    public void setReservationValues(int roomId, ReservationRequest reservationRequest) {
        try {
            log.info("Setting reservation values for roomId {}: {}", roomId, reservationRequest);
            roomAvailabilityService.setReservationValues(roomId, reservationRequest);
            log.info("Reservation values set successfully for roomId {}.", roomId);
        } catch (Exception e) {
            log.error("Failed to set reservation values for roomId {}. Reason: {}", roomId, e.getMessage());
            throw new RuntimeException("Unable to set reservation values.", e);
        }
    }

    public void synchronizeRoomData() {
        log.info("Synchronizing room data with InformationClient.");
        try {
            List<Integer> roomIds = informationClient.getRoomsId();
            log.info("Fetched {} room IDs from InformationClient. Saving to RoomAvailabilityService.", roomIds.size());
            roomAvailabilityService.saveAll(roomIds);
        } catch (Exception e) {
            log.error("Failed to synchronize room data. Reason: {}", e.getMessage());
            throw new RuntimeException("Unable to synchronize room data.", e);
        }
    }

}
