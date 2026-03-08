package com.msayeh.breeze.data.weather.repository

import com.msayeh.breeze.data.weather.mappers.toDomainModel
import com.msayeh.breeze.data.weather.remote.datasource.WeatherRemoteDataSource
import com.msayeh.breeze.domain.exception.LocalizedException
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.model.Weather
import com.msayeh.breeze.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
): WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, lon: Double): Resource<Weather> {
        return try {
            Resource.Success(remoteDataSource.getCurrentWeather(lat, lon).toDomainModel())
        } catch (e: LocalizedException) {
            Resource.Error(e)
        }
    }
}