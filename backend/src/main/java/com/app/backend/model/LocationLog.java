package com.app.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "location_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationLog {
    
    @Id
    private String id;
    
    @NotNull(message = "User ID no puede ser nulo")
    private String userId;
    
    @NotNull(message = "Latitud no puede ser nula")
    private Double latitude;
    
    @NotNull(message = "Longitud no puede ser nula")
    private Double longitude;
    
    private LocalDateTime timestamp;
}
