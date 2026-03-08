package com.msayeh.breeze.domain.repository

import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.model.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Resource<Weather>
}