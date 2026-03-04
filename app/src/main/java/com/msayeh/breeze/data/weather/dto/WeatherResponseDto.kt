package com.msayeh.breeze.data.weather.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    val weather: List<WeatherDto>,
    val main: MainWeatherDataDto,
    val wind: WindDto,
    @SerializedName("dt")
    val datetime: Long,
    val timezone: Int,
    val name: String,
)