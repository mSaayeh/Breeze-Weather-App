package com.msayeh.breeze.presentation.common

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.msayeh.breeze.domain.exception.LocalizedException
import com.msayeh.breeze.domain.model.Coordinates
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

object LocationUtils {
    fun checkLocationPermission(app: Application): Boolean =
        ContextCompat.checkSelfPermission(
            app,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    app,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    fun isLocationEnabled(app: Application): Boolean {
        val locationManager = app.getSystemService(Application.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getCurrentLocationCoordinates(app: Application): Coordinates? {
        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(app)
        val locationRequest =
            CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
        val cts = CancellationTokenSource()
        return try {
            fusedLocationProvider.getCurrentLocation(locationRequest, cts.token).await()
            .let { Coordinates(it.latitude, it.longitude) }
        } finally {
            cts.cancel()
        }
    }
}