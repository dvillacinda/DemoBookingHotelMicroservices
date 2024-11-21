package com.dv.microservices.information.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.microservices.information.model.Information;


public interface InformationRepository extends JpaRepository<Information,Long>{
    Optional<Information> findByRoomNumber(int roomNumber);
}
