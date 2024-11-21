package com.dv.microservices.reservation.service;

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
    
    public void initReservation(ReservationRequest reservationRequest){
        //map reservation request to reservation object 
        Reservation reservation = reservationRequest.toReservation();
        
        //Conversation
        int roomId = roomClient.getRoomID(reservation.getId()); 
        reservation.setRoomId(roomId);
        float price = roomClient.getPrice(roomId);
        reservation.setTotalPrice(price);

        
    }

    public void completeReservation(ReservationRequest reservationRequest){
        Reservation reservation = reservationRequest.toReservation();

        reservationRepository.save(reservation);
    }

    
}
