package com.msayeh.breeze.domain.model

data class CityWeatherDetails(
    val city: City,
    val currentWeather: Weather?,
    val forecastSlots: List<ForecastSlot>,
)
