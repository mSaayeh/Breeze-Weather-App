package com.msayeh.breeze.data.weather.remote.datasource

import com.msayeh.breeze.data.weather.remote.dto.ForecastResponseDto
import com.msayeh.breeze.data.weather.remote.dto.GeoCityDto
import com.msayeh.breeze.data.weather.remote.dto.WeatherResponseDto

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponseDto
    suspend fun getForecast(lat: Double, lon: Double): ForecastResponseDto
    suspend fun getGeoCityByCoordinates(lat: Double, lon: Double): GeoCityDto
    suspend fun getGeoCitiesByName(cityName: String): List<GeoCityDto>
}