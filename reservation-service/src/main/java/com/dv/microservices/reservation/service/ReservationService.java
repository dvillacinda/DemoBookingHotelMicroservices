package com.dv.microservices.reservation.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.model.Reservation;
import com.dv.microservices.reservation.repository.ReservationRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ReservationService {

    private final ReservationRepository reservationRepository;;
    private final CacheService cacheService;

    @Transactional(readOnly = true)
    public void initReservation(ReservationRequest reservationRequest, HttpSession session) {
        String reservationId = UUID.randomUUID().toString();

        ReservationRequest updatedRequest = new ReservationRequest(
                reservationId,
                1,
                reservationRequest.roomId(),
                reservationRequest.price(),
                reservationRequest.startDate(),
                reservationRequest.endDate(),
                reservationRequest.status(),
                reservationRequest.reservationDate(),
                reservationRequest.paymentStatus(),
                reservationRequest.cancellationReason()
        );
        cacheService.storeReservationRequest(updatedRequest.id(), updatedRequest);
        session.setAttribute("reservationId", reservationId);
    }

    @Transactional
    public void completeReservation(ReservationRequest reservationRequest) {
        Reservation reservation = reservationRequest.toReservation();

        reservationRepository.save(reservation);
        System.out.println("Reservation successfully completed: " + reservation.getId());
    }

}
