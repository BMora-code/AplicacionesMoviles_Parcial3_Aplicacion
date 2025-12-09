package com.example.aplicacion.data.network

import com.example.aplicacion.data.model.AuthResponse
import com.example.aplicacion.data.model.LocationLog
import com.example.aplicacion.data.model.LocationRequest
import com.example.aplicacion.data.model.LoginRequest
import com.example.aplicacion.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    // CORRECCIÓN: Se apunta al endpoint correcto del backend
    @POST("/api/users/me/location")
    suspend fun saveLocation(@Body locationRequest: LocationRequest): LocationLog
}