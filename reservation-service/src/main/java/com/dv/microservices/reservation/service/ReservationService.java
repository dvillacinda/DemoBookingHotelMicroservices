package com.dv.microservices.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.client.RoomClient;
import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;
import com.dv.microservices.reservation.model.Reservation;
import com.dv.microservices.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomClient roomClient;

    public void completeReservation(ReservationRequest reservationRequest) {
        Reservation reservation = reservationRequest.toReservation();

        reservationRepository.save(reservation);
        System.out.println("Reservation successfully completed: " + reservation.getId());
    }

    public List<RoomRequest> getAvailableRoom(LocalDate startDate, LocalDate endDate){
        return roomClient.getListAvailableRoom(startDate, endDate);
    }

    public void setRoomParamsToStorage(int roomId, ReservationRequest reservationRequest){
        roomClient.setReservationParamsToStorage(roomId, reservationRequest);
    }


}
