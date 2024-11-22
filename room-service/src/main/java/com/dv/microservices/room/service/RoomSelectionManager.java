package com.dv.microservices.room.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.dv.microservices.room.model.RoomAvailability;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomSelectionManager {
    private final TemporarySelectionService temporarySelectionService;
    private final ConcurrentHashMap<String, CompletableFuture<RoomAvailability>> pendingSelections = new ConcurrentHashMap<>();

    public RoomAvailability promptUserForRoomSelectionWithTimeout(
            List<RoomAvailability> availableRooms,
            String reservationId,
            int timeoutSeconds) {

        CompletableFuture<RoomAvailability> userSelectionFuture = new CompletableFuture<>();

        // Temporary room storage
        temporarySelectionService.saveAvailableRooms(reservationId, availableRooms);
        pendingSelections.put(reservationId, userSelectionFuture);

        // Configuration timeout
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            if (!userSelectionFuture.isDone()) {
                System.out.println("Timeout limit. Selection room fail.");
                userSelectionFuture.complete(null); // Timeout
            }
        }, timeoutSeconds, TimeUnit.SECONDS);

        try {
            // Locks out until user selects or timeout expires
            return userSelectionFuture.get();
        } catch (Exception e) {
            System.err.println("Error en la selección de habitación: " + e.getMessage());
            return null;
        } finally {
            pendingSelections.remove(reservationId);
        }
    }

    public boolean handleUserSelection(String reservationId, int roomId) {
        CompletableFuture<RoomAvailability> userSelectionFuture = pendingSelections.get(reservationId);

        if (userSelectionFuture == null || userSelectionFuture.isDone()) {
            return false; // Timeout limit or selection already processed
        }

        // Validate selected room
        Optional<RoomAvailability> selectedRoom = temporarySelectionService.getRoom(reservationId, roomId);
        if (selectedRoom.isPresent()) {
            userSelectionFuture.complete(selectedRoom.get());
            return true;
        }
        return false;
    }
}
