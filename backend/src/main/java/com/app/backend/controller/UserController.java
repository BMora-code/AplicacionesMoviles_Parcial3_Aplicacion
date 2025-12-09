package com.app.backend.controller;

import com.app.backend.service.UserService;
import com.app.backend.dto.UserDto;
import com.app.backend.dto.UpdateCharacterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getProfile() {
        String userId = userService.getCurrentUserId();
        UserDto user = userService.getProfile(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateCharacterVariant(@RequestBody UpdateCharacterRequest request) {
        String userId = userService.getCurrentUserId();
        UserDto user = userService.updateCharacterVariant(userId, request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<UserDto> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        String userId = userService.getCurrentUserId();
        UserDto user = userService.uploadAvatar(userId, file);
        return ResponseEntity.ok(user);
    }
}
