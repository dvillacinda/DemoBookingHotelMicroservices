package com.dv.microservices.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String initReservation(@Valid @RequestBody ReservationRequest reservationRequest){
        reservationService.initReservation(reservationRequest);
        return "Reservation start";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String completeReservation(@Valid @RequestBody ReservationRequest reservationRequest){
        reservationService.completeReservation(reservationRequest);
        return "Placed Reservation successfully";
    }
    
}
