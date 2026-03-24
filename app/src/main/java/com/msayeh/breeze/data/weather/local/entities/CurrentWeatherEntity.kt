package com.msayeh.breeze.data.weather.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "current_weather",
    foreignKeys = [ForeignKey(
        entity = CityEntity::class,
        parentColumns = ["id"],
        childColumns = ["cityId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("cityId")],
)
data class CurrentWeatherEntity(
    @PrimaryKey val cityId: Int,
    val weatherConditionId: Int,
    val tempCelsius: Double,
    val feelsLikeCelsius: Double,
    val minTempCelsius: Double,
    val maxTempCelsius: Double,
    val windSpeedMpS: Double,
    val windDeg: Int,
    val humidity: Int,
    val pressure: Int,
    val iconCode: String,
    val sunrise: Long,
    val sunset: Long,
    val datetime: Long,
    val fetchedAt: Long,
)