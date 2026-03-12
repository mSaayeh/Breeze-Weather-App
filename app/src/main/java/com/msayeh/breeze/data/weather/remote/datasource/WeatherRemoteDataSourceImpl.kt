package com.msayeh.breeze.data.weather.remote.datasource

import com.msayeh.breeze.data.utils.tryRequest
import com.msayeh.breeze.data.weather.remote.dto.ForecastResponseDto
import com.msayeh.breeze.data.weather.remote.dto.GeoCityDto
import com.msayeh.breeze.data.weather.remote.dto.WeatherResponseDto
import com.msayeh.breeze.data.weather.remote.service.GeoService
import com.msayeh.breeze.data.weather.remote.service.WeatherService
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val geoService: GeoService,
): WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponseDto =
        tryRequest {
            weatherService.getCurrentWeather(lat, lon)
        }

    override suspend fun getForecast(lat: Double, lon: Double): ForecastResponseDto = tryRequest {
        weatherService.getForecastWeather(lat, lon)
    }

    override suspend fun getGeoCityByCoordinates(
        lat: Double,
        lon: Double
    ): GeoCityDto = tryRequest {
        geoService.reverseGeocoding(lat, lon).first()
    }

    override suspend fun getGeoCitiesByName(cityName: String): List<GeoCityDto> = tryRequest {
        geoService.directGeocoding(cityName)
    }
}