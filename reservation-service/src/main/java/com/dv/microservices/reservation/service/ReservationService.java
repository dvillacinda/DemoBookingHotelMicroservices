package com.dv.microservices.reservation.service;

import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.event.ReservationEvent;
import com.dv.microservices.reservation.model.Reservation;
import com.dv.microservices.reservation.repository.ReservationRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ReservationService {

    private final ReservationRepository reservationRepository;;
    private final CacheService cacheService;
    private final KafkaTemplate<String, ReservationEvent> kafkaTemplate; 

    @Transactional(readOnly = true)
    public void initReservation(ReservationRequest reservationRequest, HttpSession session) {
        try {
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
                    reservationRequest.cancellationReason());
            cacheService.storeReservationRequest(updatedRequest.id(), updatedRequest);
            session.setAttribute("reservationId", reservationId);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while processing reservation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void completeReservation(ReservationRequest reservationRequest) {
        try {
            Reservation reservation = reservationRequest.toReservation();

            reservationRepository.save(reservation);
            System.out.println("Reservation successfully completed: " + reservation.getId());
            
            //Send the message to Kafka Topic 
            ReservationEvent event = new ReservationEvent(reservation.getId(),"rlr7dj@gmail.com"); 
            kafkaTemplate.send("reservation-placed",event); 

        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while completing reservation: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while completing reservation: " + e.getMessage(), e);
        }
    }

    
    public static String createReservationIdHash(String reservationId) {
        
        return BCrypt.hashpw(reservationId, BCrypt.gensalt(12));
    }

    
    public static boolean verifyReservationId(String providedReservationId, String storedHash) {
        return BCrypt.checkpw(providedReservationId, storedHash);
    }

}
