package com.msayeh.breeze.data.weather.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastSlotDto(
    @SerializedName("dt")
    val datetime: Long,
    val main: MainWeatherDataDto,
    val weather: List<WeatherDto>,
    val wind: WindDto,
    val fetchedAt: Long = System.currentTimeMillis(),
)
