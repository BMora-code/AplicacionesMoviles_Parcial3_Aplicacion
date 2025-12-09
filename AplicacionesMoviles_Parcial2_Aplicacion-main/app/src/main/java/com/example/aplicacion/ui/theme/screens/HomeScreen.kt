package com.example.aplicacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aplicacion.navigation.Screen
import com.example.aplicacion.ui.components.NavButton
import com.example.aplicacion.viewmodel.AppViewModel

/**
 * Pantalla de Menú Principal.
 * Se actualiza para recibir el AppViewModel compartido.
 */
@Composable
fun HomeScreen(navController: NavHostController, appViewModel: AppViewModel) { 
    val session by appViewModel.sessionState.collectAsState()
    val userEmail = session.second

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Menú Principal", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(8.dp))
            Text("Sesión activa para: $userEmail", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(Modifier.height(48.dp))

            // Botón Perfil
            NavButton(
                text = "Ver Perfil",
                icon = Icons.Default.Person,
                onClick = { navController.navigate(Screen.Profile.route) }
            )

            // Botón Ubicación
            NavButton(
                text = "Mi Ubicación (GPS)",
                icon = Icons.Default.LocationOn,
                onClick = { navController.navigate(Screen.Location.route) }
            )
        }

        // Botón Logout (Cerrar Sesión)
        OutlinedButton(
            onClick = { 
                // El ViewModel ahora maneja la limpieza de sesión y la navegación
                appViewModel.logout(navController) 
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión")
            Spacer(Modifier.width(8.dp))
            Text("Cerrar Sesión")
        }
    }
}