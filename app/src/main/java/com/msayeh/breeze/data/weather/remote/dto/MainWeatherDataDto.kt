package com.msayeh.breeze.data.weather.remote.dto

import com.google.gson.annotations.SerializedName

data class MainWeatherDataDto(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("sea_level")
    val seaLevelPressure: Int,
    @SerializedName("grnd_level")
    val groundLevePressure: Int,
)