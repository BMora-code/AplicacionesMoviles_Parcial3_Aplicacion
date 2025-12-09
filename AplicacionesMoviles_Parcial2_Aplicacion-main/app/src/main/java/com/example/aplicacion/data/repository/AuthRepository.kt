package com.example.aplicacion.data.repository

import com.example.aplicacion.data.local.TokenManager
import com.example.aplicacion.data.model.AuthResponse
import com.example.aplicacion.data.model.LoginRequest
import com.example.aplicacion.data.model.RegisterRequest
import com.example.aplicacion.data.network.ApiService

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                tokenManager.saveToken(authResponse.token)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Login failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, characterVariant: String): Result<Unit> {
        return try {
            val response = apiService.register(RegisterRequest(email, password, characterVariant))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Registration failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        tokenManager.deleteToken()
    }
}