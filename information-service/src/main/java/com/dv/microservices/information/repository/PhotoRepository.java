package com.dv.microservices.information.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.microservices.information.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo,Long>{

}
