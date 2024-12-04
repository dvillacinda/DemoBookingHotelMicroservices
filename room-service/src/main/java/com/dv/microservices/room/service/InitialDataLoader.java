package com.dv.microservices.room.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.dv.microservices.room.repository.RoomAvailabilityRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class InitialDataLoader implements ApplicationRunner{
    private final RoomAvailabilityRepository roomRepository;
    private final RoomAvailabilityOrchestrator orchestrator; 


    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        if (roomRepository.count() == 0) {
            
            orchestrator.synchronizeRoomData();
            System.out.println("Room availability data has been initialized.");
        } else {
            System.out.println("Room availability data already exists. No initialization required.");
        }
    }
}
