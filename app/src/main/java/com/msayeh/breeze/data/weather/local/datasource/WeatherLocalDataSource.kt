package com.msayeh.breeze.data.weather.local.datasource

import com.msayeh.breeze.data.weather.local.entities.AlertEntity
import com.msayeh.breeze.data.weather.local.entities.AlertWithCity
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import com.msayeh.breeze.data.weather.local.entities.CityWithWeather
import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.data.weather.local.entities.ForecastSlotEntity
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun observeAllCities(): Flow<List<CityEntity>>
    suspend fun getAllCities(): List<CityEntity>
    suspend fun getCityById(cityId: Int): CityEntity?
    suspend fun upsertCity(city: CityEntity): CityEntity
    suspend fun deleteCity(cityId: Int)
    suspend fun upsertAllCities(cities: List<CityEntity>)
    suspend fun clearCities()

    fun observeCurrentWeather(cityId: Int): Flow<CurrentWeatherEntity>
    suspend fun getCurrentWeather(cityId: Int): CurrentWeatherEntity?
    suspend fun upsertCurrentWeather(currentWeatherEntity: CurrentWeatherEntity)
    suspend fun deleteCurrentWeather(cityId: Int)

    fun observeForecastSlots(cityId: Int): Flow<List<ForecastSlotEntity>>
    suspend fun getLastForecastSlot(cityId: Int): ForecastSlotEntity?
    fun observeForecastSlotsFrom(cityId: Int, fromTime: Long): Flow<List<ForecastSlotEntity>>
    suspend fun replaceForecast(cityId: Int, forecastDays: List<ForecastSlotEntity>)

    fun observeCityWithWeather(cityId: Int): Flow<CityWithWeather?>
    fun observeAllCitiesWithWeather(): Flow<List<CityWithWeather>>

    fun observeAllAlerts(): Flow<List<AlertWithCity>>
    suspend fun getAllActiveAlerts(): List<AlertWithCity>
    fun observeAllActiveAlerts(): Flow<List<AlertWithCity>>
    suspend fun upsertAlert(alert: AlertEntity): AlertEntity
    suspend fun updateAlertEnabled(alertId: Int, enabled: Boolean)
    suspend fun deleteAlert(alertId: Int): Int
    suspend fun getAlertById(alertId: Int): AlertEntity?
}