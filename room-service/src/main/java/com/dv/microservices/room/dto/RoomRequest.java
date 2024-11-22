package com.dv.microservices.room.dto;

public record RoomRequest(
    int roomId,
    String description,
    float price
) {

}
