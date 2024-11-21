package com.dv.microservices.room.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "Information",url = "http://localhost:8084")

public interface InformationClient {
    final String CONST = "/api/info";
    @RequestMapping(method=RequestMethod.GET, value = CONST+"/get-price")
    float getPrice(@RequestParam("roomNumber") int roomNumber); 

    @RequestMapping(method = RequestMethod.GET, value = CONST+"/get-rooms-id")
    List<Integer> getRoomsId(); 
}
