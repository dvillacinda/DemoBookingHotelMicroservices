package com.dv.microservices.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.microservices.reservation.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String>{

}
