package com.dv.microservices.information.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.microservices.information.model.Information;

public interface InformationRepository extends JpaRepository<Information,Long>{

}
