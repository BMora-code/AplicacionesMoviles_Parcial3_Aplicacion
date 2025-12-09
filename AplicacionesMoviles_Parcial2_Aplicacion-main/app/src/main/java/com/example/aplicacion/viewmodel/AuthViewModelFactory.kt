package com.example.aplicacion.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.data.local.TokenManager
import com.example.aplicacion.data.UserPreferencesRepository
import com.example.aplicacion.data.dataStore
import com.example.aplicacion.data.network.RetrofitClient
import com.example.aplicacion.data.repository.AuthRepository

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val apiService = RetrofitClient.getApiService(context)
            // Usamos la importación correcta: com.example.aplicacion.data.local.TokenManager
            val tokenManager = TokenManager(context)
            val authRepository = AuthRepository(apiService, tokenManager)
            val userPrefsRepo = UserPreferencesRepository(context.dataStore)
            return AuthViewModel(authRepository, userPrefsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}