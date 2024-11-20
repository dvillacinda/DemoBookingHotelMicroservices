package com.dv.microservices.room.service;


import org.springframework.stereotype.Service;

import com.dv.microservices.room.dto.RoomAvailabilityRequest;
import com.dv.microservices.room.model.RoomAvailability;
import com.dv.microservices.room.repository.RoomAvailabilityRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RoomAvailabilityService {
    public final RoomAvailabilityRepository roomRepository;

    public void saveRoomAvailability(RoomAvailabilityRequest roomAvailabilityRequest){
        //map room availability request to room availability object 
        RoomAvailability roomAvailability = roomAvailabilityRequest.toRoomAvailability();

        //save room availability to repository
        roomRepository.save(roomAvailability);

    }
    

    
}
