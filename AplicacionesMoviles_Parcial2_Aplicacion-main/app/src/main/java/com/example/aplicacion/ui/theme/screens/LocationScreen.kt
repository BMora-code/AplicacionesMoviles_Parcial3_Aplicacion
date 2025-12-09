package com.example.aplicacion.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aplicacion.data.local.AppLocationManager
import com.example.aplicacion.viewmodel.LocationUiState
import com.example.aplicacion.viewmodel.LocationViewModel
import com.example.aplicacion.viewmodel.LocationViewModelFactory

@SuppressLint("MissingPermission")
@Composable
fun LocationScreen(navController: NavHostController) {
    val context = LocalContext.current
    val locationViewModel: LocationViewModel = viewModel(factory = LocationViewModelFactory(context))
    val uiState by locationViewModel.uiState.observeAsState(LocationUiState.Idle)
    
    // Usamos el nuevo nombre para evitar conflictos
    val appLocationManager = AppLocationManager(context)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            appLocationManager.getLastLocation { location ->
                location?.let { 
                    locationViewModel.sendLocation(it.latitude, it.longitude)
                }
            }
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Efecto para pedir la ubicación una sola vez al entrar en la pantalla
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            appLocationManager.getLastLocation { location ->
                location?.let { 
                    locationViewModel.sendLocation(it.latitude, it.longitude)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi Ubicación Actual", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(32.dp))

        when (val state = uiState) {
            is LocationUiState.Loading -> {
                CircularProgressIndicator()
                Text("Enviando ubicación...")
            }
            is LocationUiState.Success -> {
                LocationDetailsCard(locationLog = state.locationLog)
            }
            is LocationUiState.Error -> {
                Text("Error: ${state.message}", color = Color.Red)
            }
            is LocationUiState.Idle -> {
                Text("Presiona para obtener la ubicación.")
                 Button(onClick = { 
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    } else {
                        appLocationManager.getLastLocation { location ->
                            location?.let { 
                                locationViewModel.sendLocation(it.latitude, it.longitude)
                            }
                        }
                    }
                 }) {
                     Text("Obtener y Enviar Ubicación")
                 }
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Volver a Home")
        }
    }
}

@Composable
private fun LocationDetailsCard(locationLog: com.example.aplicacion.data.model.LocationLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Última Ubicación Guardada en Servidor", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Text("Latitud:", style = MaterialTheme.typography.bodyLarge)
            Text(locationLog.latitude.toString(), style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray))
            Spacer(Modifier.height(8.dp))
            Text("Longitud:", style = MaterialTheme.typography.bodyLarge)
            Text(locationLog.longitude.toString(), style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray))
            Spacer(Modifier.height(8.dp))
            Text("Fecha (Servidor):", style = MaterialTheme.typography.bodyLarge)
            Text(locationLog.timestamp, style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray))
        }
    }
}