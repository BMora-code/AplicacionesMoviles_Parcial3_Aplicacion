package com.app.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    private String id;
    
    @NotBlank(message = "Email no puede estar vacío")
    @Email(message = "Email debe ser válido")
    private String email;
    
    @NotBlank(message = "Password no puede estar vacío")
    private String passwordHash;
    
    private int characterVariant;
    
    private String profileImageUrl;
    
    private LocalDateTime createdAt;
}
