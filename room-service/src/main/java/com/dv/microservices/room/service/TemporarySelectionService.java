package com.dv.microservices.room.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.dv.microservices.room.model.RoomAvailability;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemporarySelectionService {
    private final ConcurrentHashMap<String, List<RoomAvailability>> availableRoomsMap = new ConcurrentHashMap<>();

    public void saveAvailableRooms(String reservationId, List<RoomAvailability> availableRooms) {
        availableRoomsMap.put(reservationId, availableRooms);
    }

    public Optional<RoomAvailability> getRoom(String reservationId, int roomId) {
        List<RoomAvailability> availableRooms = availableRoomsMap.get(reservationId);
        if (availableRooms != null) {
            return availableRooms.stream()
                    .filter(room -> room.getRoomId() == roomId)
                    .findFirst();
        }
        return Optional.empty();
    }

    public void clearAvailableRooms(String reservationId) {
        availableRoomsMap.remove(reservationId);
    }

}
