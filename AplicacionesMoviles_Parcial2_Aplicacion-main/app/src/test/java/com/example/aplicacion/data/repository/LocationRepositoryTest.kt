package com.example.aplicacion.data.repository

import com.example.aplicacion.data.model.LocationLog
import com.example.aplicacion.data.model.LocationRequest
import com.example.aplicacion.data.network.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class LocationRepositoryTest {

    private val apiService: ApiService = mockk()
    private val locationRepository = LocationRepository(apiService)

    @Test
    fun `saveLocation returns success when api call is successful`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val locationLog = LocationLog("id", "user", lat, lon, "time")
        val locationRequest = LocationRequest(lat, lon)

        coEvery { apiService.saveLocation(locationRequest) } returns locationLog

        // When
        val result = locationRepository.saveLocation(lat, lon)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(locationLog, result.getOrNull())
    }

    @Test
    fun `saveLocation returns failure when exception occurs`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val locationRequest = LocationRequest(lat, lon)
        val exception = IOException("Network error")

        coEvery { apiService.saveLocation(locationRequest) } throws exception

        // When
        val result = locationRepository.saveLocation(lat, lon)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
