package com.msayeh.breeze.data.weather.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    val weather: List<WeatherDto>,
    val main: MainWeatherDataDto,
    val wind: WindDto,
    val sys: WeatherSettingDto,
    @SerializedName("dt")
    val datetime: Long,
    val timezone: Int,
    @SerializedName("id")
    val cityId: Int,
    @SerializedName("name")
    val cityName: String,
    @Transient val fetchedAt: Long = 0L,
)