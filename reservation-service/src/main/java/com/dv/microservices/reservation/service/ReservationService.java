package com.dv.microservices.reservation.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.client.RoomClient;
import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.model.Reservation;
import com.dv.microservices.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository; 
    private final RoomClient roomClient; 
    
    public void saveReservation(ReservationRequest reservationRequest){
        //map reservation request to reservation object 
        Reservation reservation = reservationRequest.toReservation();
        reservation.setId(UUID.randomUUID().toString());
        
        //Conversation
        int roomId = roomClient.getRoomID(reservation.getId()); 
        reservation.setRoomId(roomId);
        float price = roomClient.getPrice(roomId);
        reservation.setTotalPrice(price);

        //save reservation to reservation repository 
        reservationRepository.save(reservation); 
    }

    
}
