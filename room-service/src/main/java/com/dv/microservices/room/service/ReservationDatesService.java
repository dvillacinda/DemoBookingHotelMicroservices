package com.dv.microservices.room.service;

import java.time.LocalDateTime;

//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dv.microservices.room.repository.ReservationDatesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationDatesService {
    private final ReservationDatesRepository rDatesRepository; 

    //@Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredReservations(LocalDateTime currentDate){
        rDatesRepository.deleteExpiredReservations(currentDate);
    }
}
