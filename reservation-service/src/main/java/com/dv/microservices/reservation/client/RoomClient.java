package com.dv.microservices.reservation.client;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import groovy.util.logging.Slf4j;

@Slf4j
public interface RoomClient {
        static final String API = "/api/room";
        Logger log = LoggerFactory.getLogger(RoomClient.class);

        @GetExchange(API + "/get-list")
        @CircuitBreaker(name = "room", fallbackMethod = "fallbackForGetListAvailableRoom")
        @Retry(name = "room")
        List<RoomRequest> getListAvailableRoom(
                        @RequestParam("startDate") String startDate,
                        @RequestParam("endDate") String endDate);

        default List<RoomRequest> fallbackForGetListAvailableRoom(String startDate, String endDate,
                        Throwable throwable) {
                log.error("Failed to retrieve available rooms for the date range {} to {}. Reason: {}", startDate,
                                endDate, throwable.getMessage());
                return Collections.emptyList();
        }

        @CircuitBreaker(name = "room", fallbackMethod = "fallbackForSetReservationParams")
        @Retry(name = "room")
        @PutExchange(API + "/set-reservation-values")
        void setReservationParamsToStorage(
                        @RequestParam("roomId") int roomId,
                        @RequestBody ReservationRequest reservationRequest);

        default void fallbackForSetReservationParams(int roomId, ReservationRequest reservationRequest,
                        Throwable throwable) {
                log.error("Failed to set reservation parameters for roomId {}. Reason: {}", roomId,
                                throwable.getMessage());
        }

}
