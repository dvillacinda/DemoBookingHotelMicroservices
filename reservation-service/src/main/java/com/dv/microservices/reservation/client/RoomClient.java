package com.dv.microservices.reservation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "Room", url= "http://localhost:8082")
public interface RoomClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/room/get-room-id")
    int getRoomID(@RequestParam String reservationId); 

    @RequestMapping(method = RequestMethod.GET, value = "/api/room/get-price")
    float getPrice(@RequestParam int roomId);

}
