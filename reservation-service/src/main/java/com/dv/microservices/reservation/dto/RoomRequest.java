package com.dv.microservices.reservation.dto;

import java.util.List;

public record RoomRequest(
    int roomId,
    String description,
    float price,
    int capacity, 
    String servicesInclude,
    List<PhotoRequest> photos

) {

}
