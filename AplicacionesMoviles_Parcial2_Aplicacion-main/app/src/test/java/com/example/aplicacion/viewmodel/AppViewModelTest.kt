package com.example.aplicacion.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.example.aplicacion.data.UserPreferencesRepository
import com.example.aplicacion.navigation.Screen
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AppViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appViewModel: AppViewModel
    private val userPrefsRepo: UserPreferencesRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Mock userFlow to avoid initialization errors in stateIn
        every { userPrefsRepo.userFlow } returns flowOf(Pair(false, ""))
        appViewModel = AppViewModel(userPrefsRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `nextCharacterVariant cycles through 0, 1, 2`() {
        assertEquals(0, appViewModel.characterVariantIndex)
        appViewModel.nextCharacterVariant()
        assertEquals(1, appViewModel.characterVariantIndex)
        appViewModel.nextCharacterVariant()
        assertEquals(2, appViewModel.characterVariantIndex)
        appViewModel.nextCharacterVariant()
        assertEquals(0, appViewModel.characterVariantIndex)
    }

    @Test
    fun `logout clears session and resets state`() = runTest(testDispatcher) {
        // Given
        val navController = mockk<NavHostController>(relaxed = true)
        
        // When
        appViewModel.logout(navController)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { userPrefsRepo.clearSession() }
        assertEquals(0, appViewModel.characterVariantIndex)
        assertNull(appViewModel.capturedImage)
        
        verify { navController.navigate(Screen.Login.route, any<NavOptionsBuilder.() -> Unit>()) }
    }
}
