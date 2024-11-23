package com.dv.microservices.room.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "Information",url = "http://localhost:8084")

public interface InformationClient {
    static final String API = "/api/info";

    @RequestMapping(method=RequestMethod.GET, value = API+"/get-price")
    float getPrice(@RequestParam("roomNumber") int roomNumber); 

    @RequestMapping(method=RequestMethod.GET, value = API+"/get-description")
    String getDescription(@RequestParam("roomNumber") int roomNumber);

    @RequestMapping(method = RequestMethod.GET, value = API+"/get-rooms-id")
    List<Integer> getRoomsId(); 
    
    @RequestMapping(method=RequestMethod.GET, value = API+"/get-services-include")
    String getServicesInclude(@RequestParam("roomNumber") int roomNumber);

    @RequestMapping(method=RequestMethod.GET, value = API+"/get-capacity")
    int getCapacity(@RequestParam("roomNumber") int roomNumber);
}
