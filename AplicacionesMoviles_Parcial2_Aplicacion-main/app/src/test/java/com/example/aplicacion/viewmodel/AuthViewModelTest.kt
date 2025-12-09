package com.example.aplicacion.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aplicacion.data.UserPreferencesRepository
import com.example.aplicacion.data.model.AuthResponse
import com.example.aplicacion.data.model.User
import com.example.aplicacion.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var authViewModel: AuthViewModel
    private val authRepository: AuthRepository = mockk()
    private val userPrefsRepo: UserPreferencesRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authViewModel = AuthViewModel(authRepository, userPrefsRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChange updates emailState correctly with valid email`() {
        val email = "test@example.com"
        authViewModel.onEmailChange(email)
        assertEquals(email, authViewModel.emailState.value)
        assertEquals(null, authViewModel.emailState.error)
    }

    @Test
    fun `onEmailChange updates emailState with error for invalid email`() {
        val email = "invalid-email"
        authViewModel.onEmailChange(email)
        assertEquals(email, authViewModel.emailState.value)
        assertTrue(authViewModel.emailState.error != null)
    }

    @Test
    fun `onPasswordChange updates passwordState correctly`() {
        val password = "Password123"
        authViewModel.onPasswordChange(password)
        assertEquals(password, authViewModel.passwordState.value)
        assertEquals(null, authViewModel.passwordState.error)
    }

    @Test
    fun `loginCompose success calls onSuccess and saves login state`() = runTest(testDispatcher) {
        // Given
        val email = "test@example.com"
        val password = "Password123"
        val authResponse = AuthResponse("token", User("1", email, 1, null))
        
        authViewModel.onEmailChange(email)
        authViewModel.onPasswordChange(password)
        
        coEvery { authRepository.login(email, password) } returns Result.success(authResponse)
        coEvery { userPrefsRepo.saveLoginState(email) } returns Unit

        var onSuccessCalled = false
        val onSuccess = { onSuccessCalled = true }
        val onFailure = { _: String -> }

        // When
        authViewModel.loginCompose(onSuccess, onFailure)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(onSuccessCalled)
        coVerify { userPrefsRepo.saveLoginState(email) }
    }

    @Test
    fun `loginCompose failure calls onFailure`() = runTest(testDispatcher) {
        // Given
        val email = "test@example.com"
        val password = "Password123"
        val errorMessage = "Login failed"
        
        authViewModel.onEmailChange(email)
        authViewModel.onPasswordChange(password)
        
        coEvery { authRepository.login(email, password) } returns Result.failure(Exception(errorMessage))

        var onFailureCalled = false
        var failureMessage = ""
        val onSuccess = { }
        val onFailure = { msg: String -> 
            onFailureCalled = true 
            failureMessage = msg
        }

        // When
        authViewModel.loginCompose(onSuccess, onFailure)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(onFailureCalled)
        assertEquals(errorMessage, failureMessage)
    }
}
