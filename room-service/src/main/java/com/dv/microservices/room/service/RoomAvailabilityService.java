package com.dv.microservices.room.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dv.microservices.room.client.InformationClient;
import com.dv.microservices.room.dto.ReservationRequest;
import com.dv.microservices.room.dto.RoomAvailabilityRequest;
import com.dv.microservices.room.dto.RoomRequest;
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

    @Transactional
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

    public String getServicesInclude(int roomId) {
        return informationClient.getServicesInclude(roomId);

    }

    public int getCapacity(int roomId) {
        return informationClient.getCapacity(roomId);

    }

    public String getDescription(int roomId) {
        return informationClient.getDescription(roomId);

    }

    @Transactional
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

    @Transactional(readOnly = true)
    public List<RoomRequest> selectAvailableRoomsRequests(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(CHECK_IN);
        LocalDateTime endDateTime = endDate.atTime(CHECK_OUT);
        String description = "";
        float price = 0f;
        String servicesInclude = "";
        int capacity = 0;

        List<RoomAvailability> roomAvailabilities = roomRepository.findAvailableRoomsBetweenDates(startDateTime,
                endDateTime);
        List<RoomRequest> roomRequests = new ArrayList<>();
        if (!roomAvailabilities.isEmpty()) {
            for (RoomAvailability room : roomAvailabilities) {

                price = getPrice(room.getRoomId());
                description = getDescription(room.getRoomId());
                servicesInclude = getServicesInclude(room.getRoomId());
                capacity = getCapacity(room.getRoomId());

                roomRequests.add(
                        new RoomRequest(
                                room.getRoomId(),
                                description,
                                price,
                                capacity,
                                servicesInclude));
            }
            return roomRequests;
        }
        return null;
    }

    @Transactional
    public void setReservationValues(int roomId, ReservationRequest request) {
        Optional<RoomAvailability> roomOptional = roomRepository.findByRoomId(roomId);
        if (roomOptional.isPresent()) {
            RoomAvailability room = roomOptional.get();

            List<ReservationDates> reservationDates = room.getReservationDates();
            if (reservationDates == null) {
                reservationDates = new ArrayList<>();
            }

            reservationDates.add(
                    new ReservationDates(request.startDate().atTime(CHECK_IN),
                            request.endDate().atTime(CHECK_OUT),
                            room));

            room.setReservationId(request.id());
            room.setReservationDates(reservationDates);

            roomRepository.save(room);
        }
    }

}
