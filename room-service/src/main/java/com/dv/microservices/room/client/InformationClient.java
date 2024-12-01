package com.dv.microservices.room.client;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InformationClient {
    static final String API = "/api/info";

    @GetExchange(API + "/get-price")
    float getPrice(@RequestParam("roomNumber") int roomNumber);

    @GetExchange(API + "/get-description")
    String getDescription(@RequestParam("roomNumber") int roomNumber);

    @GetExchange(API + "/get-rooms-id")
    List<Integer> getRoomsId();

    @GetExchange(API + "/get-services-include")
    String getServicesInclude(@RequestParam("roomNumber") int roomNumber);

    @GetExchange(API + "/get-capacity")
    int getCapacity(@RequestParam("roomNumber") int roomNumber);
}
