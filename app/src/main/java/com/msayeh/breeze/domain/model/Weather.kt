package com.msayeh.breeze.domain.model

data class Weather(
    val cityId: Int,
    val cityName: String,
    val weatherName: String,
    val iconCode: String,
    val temperature: Double,
    val feelsLike: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val windSpeed: Double,
    val windDeg: Int,
    val pressure: Int,
    val humidity: Int,
    val sunrise: Long,
    val sunset: Long,
    val timezone: Int,
)
