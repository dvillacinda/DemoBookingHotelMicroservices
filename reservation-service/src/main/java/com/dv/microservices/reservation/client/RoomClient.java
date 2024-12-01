package com.dv.microservices.reservation.client;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;

public interface RoomClient {
        static final String API = "/api/room";

        @GetExchange(API+"/get-list")
        List<RoomRequest> getListAvailableRoom(
                        @RequestParam("startDate") String startDate,
                        @RequestParam("endDate") String endDate);

        @PutExchange(API + "/set-reservation-values")
        void setReservationParamsToStorage(
                        @RequestParam("roomId") int roomId,
                        @RequestBody ReservationRequest reservationRequest);

}
