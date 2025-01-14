package com.dv.microservices.reservation.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CacheService {
    private final Map<String, List<RoomRequest>> roomRequestCache = new ConcurrentHashMap<>();
    private final Map<String, ReservationRequest> reservationRequestCache = new ConcurrentHashMap<>();
    private final Map<String, String> reservationIdHashCache = new ConcurrentHashMap<>();

    public void storeRoomRequest(String key, List<RoomRequest> value) {
        log.info("save list room with key: "+key+" value: "+value);
        roomRequestCache.put(key, value);
    }

    public List<RoomRequest> retrieveRoomRequests(String key) {
        return roomRequestCache.get(key);
    }

    public void removeRoomRequests(String key) {
        roomRequestCache.remove(key);
    }

    public void storeReservationRequest(String key, ReservationRequest reservationRequest) {
        reservationRequestCache.put(key, reservationRequest);
    }

    public ReservationRequest retrieveReservationRequest(String key) {
        return reservationRequestCache.get(key);
    }

    public void removeReservationRequest(String key) {
        reservationRequestCache.remove(key);
    }

    public void storeReservationIdHash(String key, String hash) {
        reservationIdHashCache.put(key, hash);
    }

    public String retrieveReservationIdHash(String key) {
        return reservationIdHashCache.get(key);
    }
    
    public void removeReservationIdHash(String key) {
        reservationIdHashCache.remove(key);
    }

    public void clearAllCaches() {
        roomRequestCache.clear();
        reservationRequestCache.clear();
        reservationIdHashCache.clear();
    }

}
