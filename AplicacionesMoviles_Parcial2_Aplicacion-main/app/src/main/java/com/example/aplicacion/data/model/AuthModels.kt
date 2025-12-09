package com.example.aplicacion.data.model

// Request para el endpoint de login
data class LoginRequest(
    val email: String,
    val password: String
)

// Request para el endpoint de registro
data class RegisterRequest(
    val email: String,
    val password: String,
    val characterVariant: String
)

// Respuesta de los endpoints de autenticación
data class AuthResponse(
    val token: String,
    val user: User
)

// Modelo de datos del Usuario
data class User(
    val id: String, 
    val email: String,
    val characterVariant: Int,
    val profileImageUrl: String?
)

// --- NUEVOS MODELOS PARA UBICACIÓN ---

// Request para subir la ubicación
data class LocationRequest(
    val latitude: Double,
    val longitude: Double
)

// Respuesta al subir la ubicación
data class LocationLog(
    val id: String,
    val userId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)