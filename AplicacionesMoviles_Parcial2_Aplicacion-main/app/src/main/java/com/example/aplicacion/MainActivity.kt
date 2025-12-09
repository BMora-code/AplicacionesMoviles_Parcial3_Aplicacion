package com.example.aplicacion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.aplicacion.data.local.AppLocationManager
import com.example.aplicacion.navigation.AppNavigation
import com.example.aplicacion.ui.theme.AplicacionTheme
import com.example.aplicacion.viewmodel.LocationUiState
import com.example.aplicacion.viewmodel.LocationViewModel
import com.example.aplicacion.viewmodel.LocationViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var appLocationManager: AppLocationManager
    private val locationViewModel: LocationViewModel by viewModels { LocationViewModelFactory(this) }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Se requieren permisos de ubicación para el seguimiento automático.", Toast.LENGTH_LONG).show()
        }
    }

    private val fiveMinutesInMillis = 5 * 60 * 1000L
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var periodicLocationRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        appLocationManager = AppLocationManager(this)

        setContent {
            AplicacionTheme {
                AppNavigation()
            }
        }

        observeLocationViewModel()
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
            }
            else -> {
                locationPermissionRequest.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        }
    }

    private fun startLocationUpdates() {
        // Enviar la primera ubicación inmediatamente
        sendLastKnownLocation()

        // Configurar el envío periódico
        periodicLocationRunnable = Runnable {
            sendLastKnownLocation()
            handler.postDelayed(periodicLocationRunnable, fiveMinutesInMillis)
        }
        // Iniciar el ciclo
        handler.post(periodicLocationRunnable)
    }

    private fun sendLastKnownLocation() {
        appLocationManager.getLastLocation { location ->
            if (location != null) {
                locationViewModel.sendLocation(location.latitude, location.longitude)
            } else {
                Toast.makeText(this, "No se pudo obtener la última ubicación conocida.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLocationViewModel() {
        locationViewModel.uiState.observe(this) { state ->
            when (state) {
                is LocationUiState.Loading -> {
                    Toast.makeText(this, "Enviando ubicación...", Toast.LENGTH_SHORT).show()
                }
                is LocationUiState.Success -> {
                    Toast.makeText(this, "Ubicación guardada en el servidor", Toast.LENGTH_SHORT).show()
                }
                is LocationUiState.Error -> {
                    Toast.makeText(this, "Error al enviar ubicación: ${state.message}", Toast.LENGTH_LONG).show()
                }
                else -> Unit
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Detener el handler cuando la app está en segundo plano para ahorrar batería
        handler.removeCallbacks(periodicLocationRunnable)
    }

    override fun onResume() {
        super.onResume()
        // Reanudar el handler si los permisos están concedidos
        if (::periodicLocationRunnable.isInitialized && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
             handler.post(periodicLocationRunnable)
        }
    }
}