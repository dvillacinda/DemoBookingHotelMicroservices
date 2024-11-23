package com.dv.microservices.reservation.dto;

public record RoomRequest(
    int roomId,
    String description,
    float price,
    int capacity, 
    String servicesInclude

) {

}
