package com.app.backend.service;

import com.app.backend.model.User;
import com.app.backend.repository.UserRepository;
import com.app.backend.security.JwtTokenProvider;
import com.app.backend.dto.LoginRequest;
import com.app.backend.dto.RegisterRequest;
import com.app.backend.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setCharacterVariant(1);
        user.setProfileImageUrl(null);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateToken(savedUser.getId());

        return new AuthResponse(token, savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = tokenProvider.generateToken(user.getId());
        return new AuthResponse(token, user);
    }
}
