package com.dv.microservices.room.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "Information",url = "http://localhost:8084")
public interface InformationClient {
    @RequestMapping(method=RequestMethod.GET, value = "api/info/get-price")
    float getPrice(@RequestParam int roomNumber); 
}
