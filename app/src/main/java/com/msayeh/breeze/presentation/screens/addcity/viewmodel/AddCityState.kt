package com.msayeh.breeze.presentation.screens.addcity.viewmodel

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.Coordinates

data class AddCityState(
    val isSearchLoading: Boolean = false,
    val cameraPosition: LatLng = LatLng(30.0444, 31.2357),
    val markerPosition: LatLng? = null,
    val autoCompleteCities: List<City> = emptyList(),
    val chosenCity: City? = null,
    val searchQuery: String = "",
) {
    val isSaveEnabled = chosenCity != null || markerPosition != null
}

fun LatLng.toCoordinates() =
    Coordinates(
        latitude = this.latitude,
        longitude = this.longitude
    )

fun Coordinates.toLatLng() =
    LatLng(
        this.latitude,
        this.longitude
    )