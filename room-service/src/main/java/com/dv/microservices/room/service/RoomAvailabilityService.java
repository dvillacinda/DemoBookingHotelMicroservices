package com.dv.microservices.room.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dv.microservices.room.dto.ReservationRequest;
import com.dv.microservices.room.dto.RoomAvailabilityRequest;
import com.dv.microservices.room.model.ReservationDates;
import com.dv.microservices.room.model.RoomAvailability;
import com.dv.microservices.room.repository.RoomAvailabilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomAvailabilityService {
    private final RoomAvailabilityRepository roomRepository;

    @Transactional
    public void saveRoomAvailability(RoomAvailabilityRequest roomAvailabilityRequest) {
        RoomAvailability roomAvailability = roomAvailabilityRequest.toRoomAvailability();
        roomRepository.save(roomAvailability);
    }

    @Transactional(readOnly = true)
    public List<RoomAvailability> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.of(11, 0));
        LocalDateTime endDateTime = endDate.atTime(LocalTime.of(15, 0));
        return roomRepository.findAvailableRoomsBetweenDates(startDateTime, endDateTime);
    }

    @Transactional
    public void setReservationValues(int roomId, ReservationRequest reservationRequest) {
        Optional<RoomAvailability> roomOptional = roomRepository.findByRoomId(roomId);
        if (roomOptional.isPresent()) {
            RoomAvailability room = roomOptional.get();

            ReservationDates reservationDates = new ReservationDates(
                    reservationRequest.startDate().atTime(LocalTime.of(11, 0)),
                    reservationRequest.endDate().atTime(LocalTime.of(15, 0)),
                    room
            );

            room.getReservationDates().add(reservationDates);
            room.setReservationId(reservationRequest.id());

            roomRepository.save(room);
        } else {
            throw new RuntimeException("Room not found for roomId: " + roomId);
        }
    }

    @Transactional
    public void saveAll(List<Integer> roomIds) {
        List<RoomAvailability> rooms = roomIds.stream()
                .map(id -> {
                    RoomAvailability room = new RoomAvailability();
                    room.setRoomId(id);
                    return room;
                })
                .toList();
        roomRepository.saveAll(rooms);
    }

}
