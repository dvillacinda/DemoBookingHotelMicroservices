package com.dv.microservices.information.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dv.microservices.information.dto.InformationRequest;
import com.dv.microservices.information.service.InformationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/info")
@RequiredArgsConstructor
public class InformationController {
    private final InformationService informationService; 

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String saveInformation(@Valid @RequestBody InformationRequest informationRequest){
        System.out.println(informationRequest);
        informationService.saveInformation(informationRequest);
        return "Information save successfully!"; 
    }
}