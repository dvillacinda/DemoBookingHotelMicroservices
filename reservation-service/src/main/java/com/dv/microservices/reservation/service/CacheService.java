package com.dv.microservices.reservation.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.dto.ReservationRequest;
import com.dv.microservices.reservation.dto.RoomRequest;

@Service
public class CacheService {
    private final Map<String, List<RoomRequest>> roomRequestCache = new ConcurrentHashMap<>();
    private final Map<String, ReservationRequest> reservationRequestCache = new ConcurrentHashMap<>();

    public void storeRoomRequest(String key, List<RoomRequest> value) {
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
    
    public void clearAllCaches() {
        roomRequestCache.clear();
        reservationRequestCache.clear();
    }

}
