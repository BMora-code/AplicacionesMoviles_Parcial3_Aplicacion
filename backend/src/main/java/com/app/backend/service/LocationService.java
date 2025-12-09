package com.app.backend.service;

import com.app.backend.model.LocationLog;
import com.app.backend.repository.LocationLogRepository;
import com.app.backend.dto.LocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationLogRepository locationLogRepository;

    public LocationLog saveLocation(String userId, LocationRequest request) {
        LocationLog locationLog = new LocationLog();
        locationLog.setId(UUID.randomUUID().toString());
        locationLog.setUserId(userId);
        locationLog.setLatitude(request.getLatitude());
        locationLog.setLongitude(request.getLongitude());
        locationLog.setTimestamp(LocalDateTime.now());

        return locationLogRepository.save(locationLog);
    }

    public List<LocationLog> getUserLocations(String userId) {
        return locationLogRepository.findByUserId(userId);
    }
}
