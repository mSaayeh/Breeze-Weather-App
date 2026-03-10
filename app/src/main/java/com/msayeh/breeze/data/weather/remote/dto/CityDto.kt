package com.msayeh.breeze.data.weather.remote.dto

import com.google.gson.annotations.SerializedName

data class CityDto(
    val id: Int,
    val name: String,
    @SerializedName("coord")
    val coordinates: CoordinatesDto,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val timezone: Int,
)