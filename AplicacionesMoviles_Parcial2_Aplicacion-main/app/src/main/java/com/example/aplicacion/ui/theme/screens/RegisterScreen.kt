package com.example.aplicacion.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aplicacion.navigation.Screen
import com.example.aplicacion.ui.components.ValidatedTextField
import com.example.aplicacion.utils.ValidationState
import com.example.aplicacion.viewmodel.AuthViewModel
import com.example.aplicacion.viewmodel.AuthViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    // CORRECCIÓN: Se instancia la clase AuthViewModelFactory en lugar de llamar a un método estático.
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    var confirmPasswordState by remember { mutableStateOf(ValidationState()) }

    val arePasswordsMatching: Boolean =
        authViewModel.passwordState.value == confirmPasswordState.value && confirmPasswordState.value.isNotEmpty()

    val isFormValid: Boolean =
        authViewModel.isLoginValid && arePasswordsMatching && authViewModel.characterVariantState.value.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(32.dp))

        // Campos de texto...
        ValidatedTextField(
            state = authViewModel.emailState,
            label = "Correo Electrónico",
            icon = Icons.Default.Email,
            onValueChange = authViewModel::onEmailChange
        )
        Spacer(Modifier.height(16.dp))

        ValidatedTextField(
            state = authViewModel.characterVariantState,
            label = "Variante de Personaje",
            icon = Icons.Default.Person,
            onValueChange = authViewModel::onCharacterVariantChange
        )
        Spacer(Modifier.height(16.dp))

        ValidatedTextField(
            state = authViewModel.passwordState,
            label = "Contraseña",
            icon = Icons.Default.Lock,
            onValueChange = authViewModel::onPasswordChange,
            isPassword = true
        )
        Spacer(Modifier.height(16.dp))

        ValidatedTextField(
            state = confirmPasswordState,
            label = "Confirmar Contraseña",
            icon = Icons.Default.Lock,
            onValueChange = {
                confirmPasswordState = confirmPasswordState.copy(value = it)
                if (it != authViewModel.passwordState.value) {
                    confirmPasswordState = confirmPasswordState.copy(error = "Las contraseñas no coinciden")
                } else {
                    confirmPasswordState = confirmPasswordState.copy(error = null)
                }
            },
            isPassword = true
        )
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                authViewModel.register(
                    passwordConfirmation = confirmPasswordState.value,
                    characterVariant = authViewModel.characterVariantState.value,
                    onSuccess = {
                        Toast.makeText(context, authViewModel.registrationMessage, Toast.LENGTH_LONG).show()
                        // CORRECCIÓN: Navegar a la pantalla de login de Compose
                        navController.navigate(Screen.Login.route) {
                            // Limpia la pila para que no se pueda volver al registro
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
        Spacer(Modifier.height(16.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Volver a Login")
        }
    }
}