package com.dv.microservices.room.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dv.microservices.room.model.ReservationDates;

public interface ReservationDatesRepository extends JpaRepository<ReservationDates, Long>{
    @Modifying
    @Query("DELETE FROM ReservationDates r WHERE r.endReservation < :currentDate")
    void deleteExpiredReservations(@Param("currentDate") LocalDateTime currentDate);

}
