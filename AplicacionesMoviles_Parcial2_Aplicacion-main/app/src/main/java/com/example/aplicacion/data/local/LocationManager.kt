package com.example.aplicacion.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

// RENOMBRADO para evitar conflicto con la clase nativa de Android
class AppLocationManager(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun getLastLocation(onSuccess: (Location?) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener(onSuccess)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(callback: (Location) -> Unit) {
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 10000 // 10 segundos
            fastestInterval = 5000 // 5 segundos
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach(callback)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }
}