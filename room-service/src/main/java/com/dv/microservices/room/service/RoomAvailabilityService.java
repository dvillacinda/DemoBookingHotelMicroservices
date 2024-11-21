package com.dv.microservices.room.service;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dv.microservices.room.client.InformationClient;
import com.dv.microservices.room.dto.RoomAvailabilityRequest;
import com.dv.microservices.room.model.RoomAvailability;
import com.dv.microservices.room.repository.RoomAvailabilityRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RoomAvailabilityService {
    public final RoomAvailabilityRepository roomRepository;
    public final InformationClient informationClient; 

    public void saveRoomAvailability(RoomAvailabilityRequest roomAvailabilityRequest){
        //map room availability request to room availability object 
        RoomAvailability roomAvailability = roomAvailabilityRequest.toRoomAvailability();

        //save room availability to repository
        roomRepository.save(roomAvailability);

    }

    public int getRoomID(String reservationId){
        Optional<RoomAvailability> roomOptional = roomRepository.findByReservationId(reservationId);
        if(roomOptional.isPresent()){
            RoomAvailability roomAvailability =  roomOptional.get(); 
            return roomAvailability.getRoomId(); 
        }
        return -1; 

    }

    public float getPrice(int roomId){
        return informationClient.getPrice(roomId);
        
    }
    

    
}
