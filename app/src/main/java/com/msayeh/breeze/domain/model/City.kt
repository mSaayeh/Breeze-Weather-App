package com.msayeh.breeze.domain.model

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coordinates: Coordinates,
    val isCurrentLocation: Boolean,
    val sortOrder: Int,
)
