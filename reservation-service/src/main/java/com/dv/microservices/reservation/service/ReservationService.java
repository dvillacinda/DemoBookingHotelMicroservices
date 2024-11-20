package com.dv.microservices.reservation.service;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.model.Reservation;
import com.dv.microservices.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository; 
    
    public void saveReservation(ReservationRequest reservationRequest){
        //map reservation request to reservation object 
        Reservation reservation = reservationRequest.toReservation();
        //save reservation to reservation repository 
        reservationRepository.save(reservation); 
    }
}
