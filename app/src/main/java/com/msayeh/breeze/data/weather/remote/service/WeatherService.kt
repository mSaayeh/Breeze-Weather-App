package com.msayeh.breeze.data.weather.remote.service

import com.msayeh.breeze.data.weather.remote.dto.ForecastResponseDto
import com.msayeh.breeze.data.weather.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): WeatherResponseDto

    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): ForecastResponseDto
}