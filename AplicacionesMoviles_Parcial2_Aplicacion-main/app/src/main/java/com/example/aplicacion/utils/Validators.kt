package com.example.aplicacion.utils

import java.util.regex.Pattern

/**
 * Clase de estado utilizada para manejar el valor y el error de los formularios.
 */
data class ValidationState(
    val value: String = "",
    val error: String? = null
)

/**
 * Objeto singleton que contiene la lógica de validación desacoplada.
 */
object Validators {
    // Patrón simple de expresión regular para email
    private val EMAIL_PATTERN = Pattern.compile("^\\S+@\\S+\\.\\S+\$")

    fun validateEmail(email: String): String? {
        return if (email.isEmpty()) "El correo no puede estar vacío"
        else if (!EMAIL_PATTERN.matcher(email).matches()) "Formato de correo inválido"
        else null
    }

    // Regla: al menos 6 caracteres y una mayúscula
    fun validatePassword(password: String): String? {
        return if (password.length < 6) "Mínimo 6 caracteres"
        else if (!password.any { it.isUpperCase() }) "Debe tener al menos una mayúscula"
        else null
    }
}