package com.msayeh.breeze.data.weather.remote.dto

data class WeatherSettingDto(
    val country: String,
    val sunrise: Long?,
    val sunset: Long?,
)
