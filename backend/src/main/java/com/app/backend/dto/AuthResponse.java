package com.app.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.app.backend.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserDto user;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = new UserDto(user.getId(), user.getEmail(), user.getCharacterVariant(), user.getProfileImageUrl());
    }
}
