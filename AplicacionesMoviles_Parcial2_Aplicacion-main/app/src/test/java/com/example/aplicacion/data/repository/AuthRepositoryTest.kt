package com.example.aplicacion.data.repository

import com.example.aplicacion.data.local.TokenManager
import com.example.aplicacion.data.model.AuthResponse
import com.example.aplicacion.data.model.LoginRequest
import com.example.aplicacion.data.model.RegisterRequest
import com.example.aplicacion.data.model.User
import com.example.aplicacion.data.network.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    private val apiService: ApiService = mockk()
    private val tokenManager: TokenManager = mockk(relaxed = true)
    private val authRepository = AuthRepository(apiService, tokenManager)

    @Test
    fun `login returns success when api call is successful`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val token = "token123"
        val user = User("1", email, 1, null)
        val authResponse = AuthResponse(token, user)
        val loginRequest = LoginRequest(email, password)

        coEvery { apiService.login(loginRequest) } returns Response.success(authResponse)

        // When
        val result = authRepository.login(email, password)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(authResponse, result.getOrNull())
        coVerify { tokenManager.saveToken(token) }
    }

    @Test
    fun `login returns failure when api call fails`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val loginRequest = LoginRequest(email, password)
        val errorBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { apiService.login(loginRequest) } returns Response.error(400, errorBody)

        // When
        val result = authRepository.login(email, password)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { tokenManager.saveToken(any()) }
    }

    @Test
    fun `login returns failure when exception occurs`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val loginRequest = LoginRequest(email, password)
        val exception = RuntimeException("Network error")

        coEvery { apiService.login(loginRequest) } throws exception

        // When
        val result = authRepository.login(email, password)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `register returns success when api call is successful`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val characterVariant = "variant1"
        val registerRequest = RegisterRequest(email, password, characterVariant)

        coEvery { apiService.register(registerRequest) } returns Response.success(Unit)

        // When
        val result = authRepository.register(email, password, characterVariant)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `register returns failure when api call fails`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val characterVariant = "variant1"
        val registerRequest = RegisterRequest(email, password, characterVariant)
        val errorBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { apiService.register(registerRequest) } returns Response.error(400, errorBody)

        // When
        val result = authRepository.register(email, password, characterVariant)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `logout calls deleteToken`() = runTest {
        // When
        authRepository.logout()

        // Then
        coVerify { tokenManager.deleteToken() }
    }
}
