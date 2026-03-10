package com.msayeh.breeze.domain.model

data class Weather(
    val cityId: Int,
    val temperature: Temperature,
    val feelsLike: Temperature,
    val wind: Wind,
    val humidity: Int,
    val condition: WeatherCondition,
    val pressure: Int,
    val sunCycle: SunCycle?,
    val fetchedAt: Long,
)
