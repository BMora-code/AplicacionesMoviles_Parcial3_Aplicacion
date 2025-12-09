package com.example.aplicacion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api // <-- NECESARIO
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField // <-- Esta es la API experimental
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.aplicacion.utils.ValidationState

/**
 * Campo de texto con validación en tiempo real y retroalimentación visual.
 */
@OptIn(ExperimentalMaterial3Api::class) // <-- SOLUCION: Se añade la etiqueta aquí
@Composable
fun ValidatedTextField(
    state: ValidationState,
    label: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(icon, contentDescription = null) },
            isError = state.error != null,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        // Retroalimentación visual ante errores
        if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}