package com.msayeh.breeze.data.weather.local.datasource

import android.database.sqlite.SQLiteConstraintException
import com.msayeh.breeze.data.weather.local.dao.CityDao
import com.msayeh.breeze.data.weather.local.dao.CityWeatherDao
import com.msayeh.breeze.data.weather.local.dao.WeatherDao
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import com.msayeh.breeze.data.weather.local.entities.CityWithWeather
import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.data.weather.local.entities.ForecastSlotEntity
import com.msayeh.breeze.domain.exception.CityNotFoundException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val cityDao: CityDao,
    private val cityWeatherDao: CityWeatherDao,
) : WeatherLocalDataSource {
    override fun observeAllCities(): Flow<List<CityEntity>> = cityDao.observeAllCities()

    override suspend fun getAllCities(): List<CityEntity> = cityDao.getAllCities()

    override suspend fun getCityById(cityId: Int): CityEntity? = cityDao.getCityById(cityId)

    override suspend fun upsertCity(city: CityEntity) =
        cityDao.upsertCity(city).let { cityDao.getCityById(it.toInt()) ?: throw CityNotFoundException() }

    override suspend fun deleteCity(cityId: Int) = cityDao.deleteCityById(cityId)

    override suspend fun upsertAllCities(cities: List<CityEntity>) = cityDao.upsertAllCities(cities)

    override suspend fun clearCities() = cityDao.clearCities()

    override fun observeCurrentWeather(cityId: Int): Flow<CurrentWeatherEntity> =
        weatherDao.observeCurrentWeather(cityId).map { it ?: throw CityNotFoundException() }


    override suspend fun getCurrentWeather(cityId: Int): CurrentWeatherEntity =
        weatherDao.getCurrentWeather(cityId) ?: throw CityNotFoundException()

    override suspend fun upsertCurrentWeather(currentWeatherEntity: CurrentWeatherEntity) =
        try {
            weatherDao.upsertCurrentWeather(currentWeatherEntity)
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
        }

    override suspend fun deleteCurrentWeather(cityId: Int) = weatherDao.deleteCurrentWeather(cityId)

    override fun observeForecastSlots(cityId: Int): Flow<List<ForecastSlotEntity>> =
        weatherDao.observeForecastSlots(cityId)

    override suspend fun getLastForecastSlot(cityId: Int): ForecastSlotEntity =
        weatherDao.getLastForecastSlot(cityId)

    override fun observeForecastSlotsFrom(
        cityId: Int, fromTime: Long
    ): Flow<List<ForecastSlotEntity>> = weatherDao.observeForecastSlotsFrom(cityId, fromTime)

    override suspend fun replaceForecast(
        cityId: Int, forecastDays: List<ForecastSlotEntity>
    ) = try {
        weatherDao.replaceForecast(cityId, forecastDays)
    } catch (e: SQLiteConstraintException) {
        e.printStackTrace()
    }

    override fun observeCityWithWeather(cityId: Int): Flow<CityWithWeather?> =
        cityWeatherDao.observeCityWithWeatherById(cityId)

    override fun observeAllCitiesWithWeather(): Flow<List<CityWithWeather>> =
        cityWeatherDao.observeAllCitiesWithWeather()

}