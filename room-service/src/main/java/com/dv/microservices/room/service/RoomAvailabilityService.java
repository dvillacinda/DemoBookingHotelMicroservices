package com.dv.microservices.room.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dv.microservices.room.client.InformationClient;
import com.dv.microservices.room.dto.ReservationRequest;
import com.dv.microservices.room.dto.RoomAvailabilityRequest;
import com.dv.microservices.room.model.ReservationDates;
import com.dv.microservices.room.model.RoomAvailability;
import com.dv.microservices.room.repository.RoomAvailabilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomAvailabilityService {
    public final RoomAvailabilityRepository roomRepository;
    public final InformationClient informationClient;
    private static final LocalTime CHECK_IN = LocalTime.of(11, 0, 0); // 11:00:00
    private static final LocalTime CHECK_OUT = LocalTime.of(15, 0, 0); // 15:00:00

    public void saveRoomAvailability(RoomAvailabilityRequest roomAvailabilityRequest) {
        // map room availability request to room availability object
        RoomAvailability roomAvailability = roomAvailabilityRequest.toRoomAvailability();

        // save room availability to repository
        roomRepository.save(roomAvailability);

    }

    public int getRoomID(String reservationId) {
        Optional<RoomAvailability> roomOptional = roomRepository.findByReservationId(reservationId);
        if (roomOptional.isPresent()) {
            RoomAvailability roomAvailability = roomOptional.get();
            return roomAvailability.getRoomId();
        }
        return -1;

    }

    public float getPrice(int roomId) {
        return informationClient.getPrice(roomId);

    }

    public void saveAll() {
        try {
            List<Integer> roomsId = informationClient.getRoomsId();
            for (int roomId : roomsId) {
                RoomAvailability roomAvailability = new RoomAvailability();
                roomAvailability.setRoomId(roomId);
                roomRepository.save(roomAvailability);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch or save room data: " + e.getMessage());
        }

    }

    public List<RoomAvailability> selectAvailableRooms(ReservationRequest request) {
        LocalDateTime startDate = request.startDate().atTime(CHECK_IN);
        LocalDateTime endDate = request.endDate().atTime(CHECK_OUT);

        return roomRepository.findAvailableRoomsBetweenDates(startDate, endDate);

    }

    public void updateRoomWithReservationParams(
            String reservationId, LocalDate startDate, LocalDate endDate, int roomId) {
            Optional<RoomAvailability> rOptional = roomRepository.findByRoomId(roomId);
            if(rOptional.isPresent()){
                RoomAvailability roomAvailability = rOptional.get();
                List<ReservationDates> reservationDates = new ArrayList<>(); 
                reservationDates.add(new ReservationDates(startDate.atTime(CHECK_IN), endDate.atTime(CHECK_OUT),roomAvailability));
                
                roomAvailability.setReservationId(reservationId);
                roomAvailability.setReservationDates(reservationDates);

                roomRepository.save(roomAvailability); 
            }
        
    }

}
