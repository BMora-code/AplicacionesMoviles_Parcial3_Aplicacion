package com.example.aplicacion.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.model.LocationLog
import com.example.aplicacion.data.network.RetrofitClient
import com.example.aplicacion.data.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class LocationUiState {
    object Loading : LocationUiState()
    data class Success(val locationLog: LocationLog) : LocationUiState()
    data class Error(val message: String) : LocationUiState()
    object Idle : LocationUiState()
}

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    private val _uiState = MutableLiveData<LocationUiState>(LocationUiState.Idle)
    val uiState: LiveData<LocationUiState> = _uiState

    fun sendLocation(latitude: Double, longitude: Double) {
        _uiState.value = LocationUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLocation(latitude, longitude).fold(
                onSuccess = { locationLog ->
                    _uiState.postValue(LocationUiState.Success(locationLog))
                },
                onFailure = { exception ->
                    _uiState.postValue(LocationUiState.Error(exception.message ?: "Error desconocido"))
                }
            )
        }
    }
}