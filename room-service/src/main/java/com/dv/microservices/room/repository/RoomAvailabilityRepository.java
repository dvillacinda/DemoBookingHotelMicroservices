package com.dv.microservices.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dv.microservices.room.model.RoomAvailability;

import java.util.Optional; 
import java.util.List;  
import java.time.LocalDateTime; 

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long>{
    
    Optional<RoomAvailability>  findByRoomId(int roomId);
    
    @Query("SELECT ra FROM RoomAvailability ra " +
        "WHERE ra.id NOT IN (" +
        "    SELECT r.roomAvailability.id FROM ReservationDates r " +
        "    WHERE (r.startReservation < :endDate AND r.endReservation > :startDate)" +
        ") " +
        "AND ra.id NOT IN (" +
        "    SELECT b.roomAvailability.id FROM BlockedPeriod b " +
        "    WHERE (b.startBlocked < :endDate AND b.endBlocked > :startDate)" +
        ")")
    List<RoomAvailability> findAvailableRoomsBetweenDates(
        @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    
}
