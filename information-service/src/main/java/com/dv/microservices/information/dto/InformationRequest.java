package com.dv.microservices.information.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.dv.microservices.information.model.Information;
import com.dv.microservices.information.model.Photo;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InformationRequest(
    @NotNull(message = "Room number can't be null")
    @Positive(message = "Room should be high than zero")
    @JsonProperty("room_number")
    Integer roomNumber,

    @NotNull(message = "Capacity number can't be null")
    @Positive(message = "Capacity should be high than zero")
    Integer capacity,

    @NotBlank(message = "Description can't be blank")
    String description,

    @NotNull(message = "Price can't be null")
    @Positive(message = "Price should be high than zero")
    Float price,

    @NotBlank(message = "Service include can't be blank")
    @JsonProperty("service_include")
    String servicesInclude,

    @Valid List<PhotoRequest> photoRequests

) {

    public Information toInformation(){
        Information info = new Information();
        info.setCapacity(this.capacity());
        info.setDescription(this.description());
        info.setPrice(this.price());
        info.setRoomNumber(this.roomNumber());
        info.setServicesInclude(this.servicesInclude());
        List<Photo> photos = null; 
        if(photoRequests!=null && !photoRequests.isEmpty() ){
            photos = photoRequests.stream()
            .map(pRequest-> new Photo(pRequest.url(), info))
            .collect(Collectors.toList());
        }
        info.setPhotos(photos);
        return info; 
    } 
    

}
