package com.example.aplicacion.data.repository

import com.example.aplicacion.data.model.LocationLog
import com.example.aplicacion.data.model.LocationRequest
import com.example.aplicacion.data.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class LocationRepository(private val apiService: ApiService) {

    suspend fun saveLocation(latitude: Double, longitude: Double): Result<LocationLog> {
        return try {
            val response = apiService.saveLocation(LocationRequest(latitude, longitude))
            Result.success(response)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }
}