package com.example.aplicacion.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.aplicacion.data.model.LocationLog
import com.example.aplicacion.data.repository.LocationRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
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
class LocationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var locationViewModel: LocationViewModel
    private val locationRepository: LocationRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        locationViewModel = LocationViewModel(locationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendLocation updates uiState to Success on repository success`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val locationLog = LocationLog("id", "user", lat, lon, "time")
        
        coEvery { locationRepository.saveLocation(lat, lon) } returns Result.success(locationLog)

        val observer = mockk<Observer<LocationUiState>>(relaxed = true)
        locationViewModel.uiState.observeForever(observer)

        // When
        locationViewModel.sendLocation(lat, lon)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify { observer.onChanged(LocationUiState.Loading) }
        verify { observer.onChanged(LocationUiState.Success(locationLog)) }
        
        locationViewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `sendLocation updates uiState to Error on repository failure`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val errorMessage = "Network error"
        
        coEvery { locationRepository.saveLocation(lat, lon) } returns Result.failure(Exception(errorMessage))

        val observer = mockk<Observer<LocationUiState>>(relaxed = true)
        locationViewModel.uiState.observeForever(observer)

        // When
        locationViewModel.sendLocation(lat, lon)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify { observer.onChanged(LocationUiState.Loading) }
        verify { observer.onChanged(LocationUiState.Error(errorMessage)) }
        
        locationViewModel.uiState.removeObserver(observer)
    }
}
