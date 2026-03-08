package com.msayeh.breeze.data.weather.remote.datasource

import com.msayeh.breeze.data.utils.tryRequest
import com.msayeh.breeze.data.weather.remote.dto.WeatherResponseDto
import com.msayeh.breeze.data.weather.remote.service.WeatherService
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
): WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponseDto {
        return tryRequest {
            weatherService.getCurrentWeather(lat, lon)
        }
    }
}