package com.example.aplicacion.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.data.network.RetrofitClient
import com.example.aplicacion.data.repository.LocationRepository

class LocationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            val apiService = RetrofitClient.getApiService(context)
            val repository = LocationRepository(apiService)
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}