package com.app.backend.controller;

import com.app.backend.service.LocationService;
import com.app.backend.service.UserService;
import com.app.backend.model.LocationLog;
import com.app.backend.dto.LocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users/me/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<LocationLog> saveLocation(@Valid @RequestBody LocationRequest request) {
        String userId = userService.getCurrentUserId();
        LocationLog location = locationService.saveLocation(userId, request);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<List<LocationLog>> getUserLocations() {
        String userId = userService.getCurrentUserId();
        List<LocationLog> locations = locationService.getUserLocations(userId);
        return ResponseEntity.ok(locations);
    }
}
