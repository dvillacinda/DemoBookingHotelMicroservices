package com.dv.microservices.reservation.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.dv.microservices.reservation.dto.RoomRequest;

@Service
public class CacheService {
    private final Map<String, List<RoomRequest>> cache = new ConcurrentHashMap<>();

    public void store(String key, List<RoomRequest> value) {
        cache.put(key, value);
    }

    public List<RoomRequest> retrieve(String key) {
        return cache.get(key);
    }

}
