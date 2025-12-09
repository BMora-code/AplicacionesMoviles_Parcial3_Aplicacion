package com.example.aplicacion.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.aplicacion.navigation.Screen
import com.example.aplicacion.ui.components.ValidatedTextField
import com.example.aplicacion.viewmodel.AuthViewModel
import com.example.aplicacion.viewmodel.AuthViewModelFactory

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    // CORRECCIÓN: Se instancia la clase AuthViewModelFactory en lugar de llamar a un método estático.
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(32.dp))

        ValidatedTextField(
            state = authViewModel.emailState,
            label = "Correo Electrónico",
            icon = Icons.Default.Email,
            onValueChange = authViewModel::onEmailChange
        )
        Spacer(Modifier.height(16.dp))

        ValidatedTextField(
            state = authViewModel.passwordState,
            label = "Contraseña",
            icon = Icons.Default.Lock,
            onValueChange = authViewModel::onPasswordChange,
            isPassword = true
        )
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                authViewModel.loginCompose(
                    onSuccess = {
                        // La navegación se dispara sí o sí
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
            },
            enabled = authViewModel.isLoginValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
        Spacer(Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}