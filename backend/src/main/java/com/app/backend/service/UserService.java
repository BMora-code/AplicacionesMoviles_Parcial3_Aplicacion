package com.app.backend.service;

import com.app.backend.model.User;
import com.app.backend.repository.UserRepository;
import com.app.backend.dto.UserDto;
import com.app.backend.dto.UpdateCharacterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/avatars/";

    public UserDto getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UserDto(user.getId(), user.getEmail(), user.getCharacterVariant(), user.getProfileImageUrl());
    }

    public UserDto updateCharacterVariant(String userId, UpdateCharacterRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setCharacterVariant(request.getCharacterVariant());
        User updatedUser = userRepository.save(user);
        return new UserDto(updatedUser.getId(), updatedUser.getEmail(), updatedUser.getCharacterVariant(), updatedUser.getProfileImageUrl());
    }

    public UserDto uploadAvatar(String userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear directorio si no existe
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generar nombre único para el archivo
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filepath = UPLOAD_DIR + filename;

        // Guardar archivo
        file.transferTo(new File(filepath));

        // Actualizar URL de imagen del usuario
        user.setProfileImageUrl("/api/files/" + filename);
        User updatedUser = userRepository.save(user);

        return new UserDto(updatedUser.getId(), updatedUser.getEmail(), updatedUser.getCharacterVariant(), updatedUser.getProfileImageUrl());
    }

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (String) authentication.getPrincipal();
        }
        throw new RuntimeException("No hay usuario autenticado");
    }
}
