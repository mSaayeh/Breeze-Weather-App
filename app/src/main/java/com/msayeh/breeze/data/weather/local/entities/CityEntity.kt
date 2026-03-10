package com.msayeh.breeze.data.weather.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean,
    val sortOrder: Int,
    val addedAt: Long = System.currentTimeMillis(),
)
