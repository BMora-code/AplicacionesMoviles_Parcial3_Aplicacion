package com.example.aplicacion.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidatorsTest {

    // --- Pruebas para validateEmail ---

    @Test
    fun `validateEmail returns null for valid email`() {
        val email = "test@example.com"
        val result = Validators.validateEmail(email)
        assertNull("Debería retornar null para un correo válido", result)
    }

    @Test
    fun `validateEmail returns error for empty email`() {
        val email = ""
        val result = Validators.validateEmail(email)
        assertEquals("El correo no puede estar vacío", result)
    }

    @Test
    fun `validateEmail returns error for invalid format no at symbol`() {
        val email = "testexample.com"
        val result = Validators.validateEmail(email)
        assertEquals("Formato de correo inválido", result)
    }

    @Test
    fun `validateEmail returns error for invalid format no domain dot`() {
        val email = "test@examplecom"
        val result = Validators.validateEmail(email)
        assertEquals("Formato de correo inválido", result)
    }

    // --- Pruebas para validatePassword ---

    @Test
    fun `validatePassword returns null for valid password`() {
        // Mínimo 6 caracteres y al menos una mayúscula
        val password = "Password123"
        val result = Validators.validatePassword(password)
        assertNull("Debería retornar null para una contraseña válida", result)
    }

    @Test
    fun `validatePassword returns error for short password`() {
        val password = "Pass"
        val result = Validators.validatePassword(password)
        assertEquals("Mínimo 6 caracteres", result)
    }

    @Test
    fun `validatePassword returns error for password without uppercase`() {
        val password = "password123"
        val result = Validators.validatePassword(password)
        assertEquals("Debe tener al menos una mayúscula", result)
    }

    @Test
    fun `validatePassword returns null for password exactly 6 chars with uppercase`() {
        val password = "Pass12"
        val result = Validators.validatePassword(password)
        assertNull(result)
    }

    // --- Pruebas para ValidationState ---

    @Test
    fun `ValidationState has correct default values`() {
        val state = ValidationState()
        assertEquals("", state.value)
        assertNull(state.error)
    }
}
