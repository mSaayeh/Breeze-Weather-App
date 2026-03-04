package com.msayeh.breeze.data.weather.remote.datasource

import com.msayeh.breeze.data.weather.remote.dto.WeatherResponseDto
import com.msayeh.breeze.data.weather.remote.service.WeatherService
import javax.inject.Inject

class WeatherLocalDataSource @Inject constructor(
    private val weatherService: WeatherService
) {
    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponseDto {
        TODO()
    }
}