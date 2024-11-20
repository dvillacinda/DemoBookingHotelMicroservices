package com.dv.microservices.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.microservices.user.model.ReservationHistory;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory,Long>{

}
