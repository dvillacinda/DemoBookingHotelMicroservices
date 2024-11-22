package com.dv.microservices.reservation.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;

@FeignClient(value = "Room", url= "http://localhost:8082")
public interface RoomClient {
    static final String API = "/api/room"; 

    @RequestMapping(method = RequestMethod.PUT, value = API+"/process-room-selection")
    ReservationRequest getRoomParams(@RequestBody ReservationRequest request);
    
    @RequestMapping(method = RequestMethod.GET, value= API+"/get-list")
    List<RoomRequest> getListAvailableRoom(@RequestParam LocalDate startDate, @RequestParam LocalDate enDate); 

}


