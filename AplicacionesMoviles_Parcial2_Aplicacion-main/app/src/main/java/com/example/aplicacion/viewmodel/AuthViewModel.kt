package com.example.aplicacion.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.local.TokenManager
import com.example.aplicacion.data.UserPreferencesRepository
import com.example.aplicacion.data.dataStore
import com.example.aplicacion.data.model.AuthResponse
import com.example.aplicacion.data.model.User
import com.example.aplicacion.data.network.RetrofitClient
import com.example.aplicacion.data.repository.AuthRepository
import com.example.aplicacion.utils.ValidationState
import com.example.aplicacion.utils.Validators
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val token: String, val user: User?) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userPrefsRepo: UserPreferencesRepository
) : ViewModel() {

    var emailState by mutableStateOf(ValidationState())
    var passwordState by mutableStateOf(ValidationState())
    var characterVariantState by mutableStateOf(ValidationState())
    var registrationMessage by mutableStateOf<String?>(null)

    val isLoginValid: Boolean
        get() = emailState.error == null && passwordState.error == null &&
                emailState.value.isNotEmpty() && passwordState.value.isNotEmpty()

    fun onEmailChange(newEmail: String) {
        emailState = emailState.copy(value = newEmail, error = Validators.validateEmail(newEmail))
    }

    fun onPasswordChange(newPassword: String) {
        passwordState = passwordState.copy(value = newPassword, error = Validators.validatePassword(newPassword))
    }

    fun onCharacterVariantChange(newVariant: String) {
        characterVariantState = characterVariantState.copy(value = newVariant, error = if (newVariant.isEmpty()) "El campo no puede estar vacío" else null)
    }

    fun register(passwordConfirmation: String, characterVariant: String, onSuccess: () -> Unit) {
        if (isLoginValid && passwordState.value == passwordConfirmation) {
            viewModelScope.launch {
                authRepository.register(emailState.value, passwordState.value, characterVariant)
                    .onSuccess { 
                        registrationMessage = "Registro exitoso"
                        onSuccess() 
                    }
                    .onFailure { registrationMessage = "Error en registro: ${it.message}" }
            }
        } else {
            registrationMessage = "Error: Verifica los campos"
        }
    }

    fun loginCompose(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (!isLoginValid) return
        viewModelScope.launch {
            val result = authRepository.login(emailState.value, passwordState.value)
            result.fold(
                onSuccess = {
                    try {
                        // Intenta guardar la sesión, pero no dejes que bloquee la navegación
                        userPrefsRepo.saveLoginState(emailState.value)
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Fallo al guardar estado de sesión, pero se procede a navegar.", e)
                    }
                    // Dispara la navegación SI O SI
                    onSuccess()
                },
                onFailure = {
                    onFailure(it.message ?: "Error desconocido")
                }
            )
        }
    }
    
    // --- Código para ViewBinding --- 
    private val _uiState = MutableLiveData<AuthUiState>(AuthUiState.Idle)
    val uiState: LiveData<AuthUiState> = _uiState

    fun loginViewBinding(email: String, password: String) {
         if (email.isEmpty() || password.isEmpty()) {
            _uiState.value = AuthUiState.Error("Por favor completa todos los campos")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
             authRepository.login(email, password).fold(
                onSuccess = { authResponse ->
                    userPrefsRepo.saveLoginState(email)
                    _uiState.postValue(AuthUiState.Success(authResponse.token, authResponse.user))
                },
                onFailure = { 
                    _uiState.postValue(AuthUiState.Error(it.message ?: "Error desconocido"))
                }
            )
        }
    }
}