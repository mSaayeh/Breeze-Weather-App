package com.msayeh.breeze.data.weather.remote.datasource

import com.msayeh.breeze.data.weather.remote.dto.WeatherResponseDto

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponseDto
}