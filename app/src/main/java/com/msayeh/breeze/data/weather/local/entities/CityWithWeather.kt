package com.msayeh.breeze.data.weather.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CityWithWeather(
    @Embedded val city: CityEntity,
    @Relation(parentColumn = "id", entityColumn = "cityId")
    val currentWeather: CurrentWeatherEntity?,
    @Relation(parentColumn = "id", entityColumn = "cityId")
    val forecastSlots: List<ForecastSlotEntity>,
)
