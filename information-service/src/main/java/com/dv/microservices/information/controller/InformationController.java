package com.dv.microservices.information.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dv.microservices.information.dto.InformationRequest;
import com.dv.microservices.information.model.Information;
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

    @GetMapping("/get-price")
    @ResponseStatus(HttpStatus.OK)
    public float getPrice(@RequestParam int roomNumber){
        return informationService.getPrice(roomNumber);
    }

    @GetMapping("/get-description")
    @ResponseStatus(HttpStatus.OK)
    public String getDescription(@RequestParam int roomNumber){
        return informationService.getDescription(roomNumber);
    }

    @GetMapping("/get-rooms-id")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getAllRoomsId(){
        List<Integer> roomIds = new ArrayList<>();
        for(Information information : informationService.getAllInformations()){
            roomIds.add(information.getRoomNumber());
        }
        return roomIds; 
    }
}
