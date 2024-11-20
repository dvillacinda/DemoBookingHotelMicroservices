package com.dv.microservices.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.microservices.room.model.BlockedPeriod;

public interface BlockedRepository extends JpaRepository<BlockedPeriod, Long>{

}
