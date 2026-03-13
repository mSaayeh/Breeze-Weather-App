package com.msayeh.breeze.data.weather.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param id is -1 when the city is added as the current user location
 */
@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val sortOrder: Int,
    val addedAt: Long = System.currentTimeMillis(),
)
