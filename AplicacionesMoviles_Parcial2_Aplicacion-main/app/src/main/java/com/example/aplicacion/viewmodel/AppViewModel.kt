package com.example.aplicacion.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.aplicacion.data.UserPreferencesRepository
import com.example.aplicacion.data.dataStore
import com.example.aplicacion.navigation.Screen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val repo: UserPreferencesRepository) : ViewModel() {

    val sessionState: StateFlow<Pair<Boolean, String>> =
        repo.userFlow.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            Pair(false, "")
        )

    var capturedImage by mutableStateOf<Bitmap?>(null)
    var characterVariantIndex by mutableStateOf(0)
        private set
        
    fun nextCharacterVariant() {
        characterVariantIndex = (characterVariantIndex + 1) % 3
    }

    fun logout(navController: NavHostController) {
        viewModelScope.launch {
            repo.clearSession()
            capturedImage = null
            characterVariantIndex = 0
            
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    companion object {
        fun factory(context: Context): androidx.lifecycle.ViewModelProvider.Factory =
            object : androidx.lifecycle.ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppViewModel(UserPreferencesRepository(context.dataStore)) as T
                }
            }
    }
}