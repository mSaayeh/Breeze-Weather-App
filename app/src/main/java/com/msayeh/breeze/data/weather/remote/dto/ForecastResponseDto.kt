package com.msayeh.breeze.data.weather.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastResponseDto(
    @SerializedName("list")
    val forecastsList: List<ForecastSlotDto>,
    val city: CityDto,
    @Transient val fetchedAt: Long = 0L,
)
