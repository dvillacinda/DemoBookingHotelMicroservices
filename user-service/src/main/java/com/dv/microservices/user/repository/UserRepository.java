package com.dv.microservices.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.microservices.user.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

}
