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
    val id: Int,
    val name: String,
)