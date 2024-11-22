package com.dv.microservices.information.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dv.microservices.information.dto.InformationRequest;
import com.dv.microservices.information.model.Information;
import com.dv.microservices.information.repository.InformationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InformationService {
    private final InformationRepository informationRepository; 

    public void saveInformation(InformationRequest informationRequest){
        Information info = informationRequest.toInformation();

        informationRepository.save(info);

    }

    public float getPrice(int roomNumber){
        Optional<Information> iOptional = informationRepository.findByRoomNumber(roomNumber);
        if(iOptional.isPresent()){
            Information information = iOptional.get(); 
            return information.getPrice(); 
        }
        return 0.0f; 
    }

    public String getDescription(int roomNumber){
        Optional<Information> iOptional = informationRepository.findByRoomNumber(roomNumber);
        if(iOptional.isPresent()){
            Information information = iOptional.get();
            return information.getDescription();
        }
        return null; 
    }

    public List<Information> getAllInformations(){
        return informationRepository.findAll(); 
    }
}
