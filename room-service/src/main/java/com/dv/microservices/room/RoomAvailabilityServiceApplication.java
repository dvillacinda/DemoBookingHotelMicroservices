package com.dv.microservices.room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RoomAvailabilityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomAvailabilityServiceApplication.class, args);
	}

}
