package com.dv.microservices.information.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dv.microservices.information.dto.InformationRequest;
import com.dv.microservices.information.exceptions.NotFoundException;
import com.dv.microservices.information.model.Information;
import com.dv.microservices.information.repository.InformationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InformationService {
    private final InformationRepository informationRepository;

    @Transactional
    public void saveInformation(InformationRequest informationRequest) {
        try {
            Information info = informationRequest.toInformation();

            informationRepository.save(info);
        } catch (Exception e) {
            throw new RuntimeException("Not save Information succesfully",e);
        }

    }

    public float getPrice(int roomNumber) {
        Optional<Information> iOptional = informationRepository.findByRoomNumber(roomNumber);
        if (iOptional.isPresent()) {
            Information information = iOptional.get();
            return information.getPrice();
        }
        throw new NotFoundException("Not find any room with this room number: "+roomNumber); 
    }

    public String getDescription(int roomNumber) {
        Optional<Information> iOptional = informationRepository.findByRoomNumber(roomNumber);
        if (iOptional.isPresent()) {
            Information information = iOptional.get();
            return information.getDescription();
        }
        throw new NotFoundException("Not find any room with this room number: "+roomNumber); 
    }

    public String getServicesInclude(int roomNumber) {
        Optional<Information> iOptional = informationRepository.findByRoomNumber(roomNumber);
        if (iOptional.isPresent()) {
            Information information = iOptional.get();
            return information.getServicesInclude();
        }
        throw new NotFoundException("Not find any room with this room number: "+roomNumber); 
    }

    public int getCapacity(int roomNumber) {
        Optional<Information> iOptional = informationRepository.findByRoomNumber(roomNumber);
        if (iOptional.isPresent()) {
            Information information = iOptional.get();
            return information.getCapacity();
        }
        throw new NotFoundException("Not find any room with this room number: "+roomNumber); 
    }

    public List<Information> getAllInformations() {
        return informationRepository.findAll();
    }
}
