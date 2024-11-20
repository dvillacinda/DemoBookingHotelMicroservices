package com.dv.microservices.information.service;

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
}
