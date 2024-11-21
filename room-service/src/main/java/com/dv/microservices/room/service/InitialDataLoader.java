package com.dv.microservices.room.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.dv.microservices.room.repository.RoomAvailabilityRepository;

@Component
public class InitialDataLoader implements ApplicationRunner{
    private final RoomAvailabilityRepository roomRepository;
    private final RoomAvailabilityService roomAvailabilityService;

    public InitialDataLoader(RoomAvailabilityRepository roomRepository, RoomAvailabilityService roomAvailabilityService) {
        this.roomRepository = roomRepository;
        this.roomAvailabilityService = roomAvailabilityService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        if (roomRepository.count() == 0) {
            
            roomAvailabilityService.saveAll();
            System.out.println("Room availability data has been initialized.");
        } else {
            System.out.println("Room availability data already exists. No initialization required.");
        }
    }
}
