package com.dv.microservices.room.client;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.dv.microservices.room.dto.PhotoRequest;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Slf4j
public interface InformationClient {
    static final String API = "/api/info";
    Logger log = LoggerFactory.getLogger(InformationClient.class);

    @GetExchange(API + "/get-price")
    @CircuitBreaker(name = "information", fallbackMethod = "fallbackGetPrice")
    @Retry(name = "information")
    float getPrice(@RequestParam("roomNumber") int roomNumber);

    default float fallbackGetPrice(int roomNumber, Throwable throwable) {
        log.error("Failed to retrieve price for roomNumber {}. Reason: {}", roomNumber, throwable.getMessage());
        return -1; 
    }

    @GetExchange(API + "/get-description")
    @CircuitBreaker(name = "information", fallbackMethod = "fallbackGetDescription")
    @Retry(name = "information")
    String getDescription(@RequestParam("roomNumber") int roomNumber);

    default String fallbackGetDescription(int roomNumber, Throwable throwable) {
        log.error("Failed to retrieve description for roomNumber {}. Reason: {}", roomNumber, throwable.getMessage());
        return "Description not available"; 
    }

    @GetExchange(API + "/get-rooms-id")
    @CircuitBreaker(name = "information", fallbackMethod = "fallbackGetRoomsId")
    @Retry(name = "information")
    List<Integer> getRoomsId();

    default List<Integer> fallbackGetRoomsId(Throwable throwable) {
        log.error("Failed to retrieve room IDs. Reason: {}", throwable.getMessage());
        return Collections.emptyList(); 
    }

    @GetExchange(API + "/get-services-include")
    @CircuitBreaker(name = "information", fallbackMethod = "fallbackGetServicesInclude")
    @Retry(name = "information")
    String getServicesInclude(@RequestParam("roomNumber") int roomNumber);

    default String fallbackGetServicesInclude(int roomNumber, Throwable throwable) {
        log.error("Failed to retrieve services for roomNumber {}. Reason: {}", roomNumber, throwable.getMessage());
        return "Services information not available"; 
    }

    @GetExchange(API + "/get-capacity")
    @CircuitBreaker(name = "information", fallbackMethod = "fallbackGetCapacity")
    @Retry(name = "information")
    int getCapacity(@RequestParam("roomNumber") int roomNumber);

    default int fallbackGetCapacity(int roomNumber, Throwable throwable) {
        log.error("Failed to retrieve capacity for roomNumber {}. Reason: {}", roomNumber, throwable.getMessage());
        return -1; 
    }

    @GetExchange(API + "/get-photo")
    @CircuitBreaker(name = "information", fallbackMethod = "fallbackGetPhoto")
    @Retry(name = "information")
    List<PhotoRequest> getPhoto(@RequestParam("roomNumber") int roomNumber);

    default List<PhotoRequest> fallbackGetPhoto(int roomNumber, Throwable throwable) {
        log.error("Failed to retrieve photos for roomNumber {}. Reason: {}", roomNumber, throwable.getMessage());
        return Collections.emptyList();  
    }
}

