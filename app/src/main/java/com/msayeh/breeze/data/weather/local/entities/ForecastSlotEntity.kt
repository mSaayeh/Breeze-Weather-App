package com.msayeh.breeze.data.weather.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "forecast_slots",
    foreignKeys = [ForeignKey(
        entity = CityEntity::class,
        parentColumns = ["id"],
        childColumns = ["cityId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("cityId")],
)
data class ForecastSlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cityId: Int,
    val weatherName: String,
    val datetime: Long,
    val tempCelsius: Double,
    val feelsLikeCelsius: Double,
    val minTempCelsius: Double,
    val maxTempCelsius: Double,
    val windSpeedMpS: Double,
    val windDeg: Int,
    val humidity: Int,
    val pressure: Int,
    val iconCode: String,
    val fetchedAt: Long,
)