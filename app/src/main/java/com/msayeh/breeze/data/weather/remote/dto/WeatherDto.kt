package com.msayeh.breeze.data.weather.remote.dto

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)
