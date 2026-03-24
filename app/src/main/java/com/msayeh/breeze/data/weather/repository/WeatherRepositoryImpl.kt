package com.msayeh.breeze.data.weather.repository

import com.msayeh.breeze.R
import com.msayeh.breeze.data.utils.CacheUtils
import com.msayeh.breeze.data.weather.local.datasource.WeatherLocalDataSource
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import com.msayeh.breeze.data.weather.mappers.toDomainModel
import com.msayeh.breeze.data.weather.mappers.toEntity
import com.msayeh.breeze.data.weather.mappers.toSlotEntities
import com.msayeh.breeze.data.weather.remote.datasource.WeatherRemoteDataSource
import com.msayeh.breeze.domain.exception.AlertNotFoundException
import com.msayeh.breeze.domain.exception.CityNotFoundException
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.AlertCityDetails
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.CityWeatherDetails
import com.msayeh.breeze.domain.model.Coordinates
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.model.tryResourceSuspend
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.alerts.scheduler.AlertSchedulerManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val schedulerManager: AlertSchedulerManager,
) : WeatherRepository {
    override fun observeAllCities(): Flow<List<City>> =
        localDataSource.observeAllCities().map { cities -> cities.map { it.toDomainModel() } }


    override fun observeCityWithWeather(cityId: Int): Flow<CityWeatherDetails?> =
        localDataSource.observeCityWithWeather(cityId).map { it?.toDomainModel() }


    override fun observeAllCitiesWithWeather(): Flow<List<CityWeatherDetails>> =
        localDataSource.observeAllCitiesWithWeather()
            .map { cities -> cities.map { it.toDomainModel() } }

    override suspend fun getCitySuggestions(query: String): Resource<List<City>> =
        tryResourceSuspend {
            remoteDataSource.getGeoCitiesByName(query)
                .map { it.toDomainModel(isCurrentLocation = false) }
        }

    override suspend fun getCityByCoordinates(
        coordinates: Coordinates, isCurrentLocation: Boolean
    ): Resource<City> = tryResourceSuspend {
        remoteDataSource.getGeoCityByCoordinates(
            coordinates.latitude, coordinates.longitude
        ).toDomainModel(isCurrentLocation)
    }

    override suspend fun addCityToFavorites(city: City): Resource<City> = tryResourceSuspend {
        localDataSource.upsertCity(
            if (city.isCurrentLocation) CityEntity(
                id = -1,
                name = city.name,
                country = city.country,
                latitude = city.coordinates.latitude,
                longitude = city.coordinates.longitude,
                sortOrder = city.sortOrder
            )
            else CityEntity(
                name = city.name,
                country = city.country,
                latitude = city.coordinates.latitude,
                longitude = city.coordinates.longitude,
                sortOrder = city.sortOrder
            )
        ).toDomainModel()
    }

    override suspend fun removeCityFromFavorites(cityId: Int): Resource<Unit> =
        tryResourceSuspend(R.string.error_removing_city_error) {
            localDataSource.deleteCity(cityId)
        }

    override suspend fun refreshWeather(cityId: Int): Resource<Unit> = tryResourceSuspend {
        val city = localDataSource.getCityById(cityId) ?: throw CityNotFoundException()
        refreshCurrentWeather(city)
        refreshForecast(city)
    }

    override suspend fun refreshCurrentWeather(cityId: Int): Resource<Unit> = tryResourceSuspend {
        val city = localDataSource.getCityById(cityId) ?: throw CityNotFoundException()
        refreshCurrentWeather(city)
    }

    private suspend fun refreshWeather(cityEntity: CityEntity) {
        refreshCurrentWeather(cityEntity)
        refreshForecast(cityEntity)
    }

    private suspend fun refreshCurrentWeather(cityEntity: CityEntity) {
        val currentWeather =
            remoteDataSource.getCurrentWeather(cityEntity.latitude, cityEntity.longitude)
        localDataSource.upsertCurrentWeather(currentWeather.toEntity(cityEntity.id))
    }

    private suspend fun refreshForecast(cityEntity: CityEntity) {
        val forecast = remoteDataSource.getForecast(cityEntity.latitude, cityEntity.longitude)
        localDataSource.replaceForecast(cityEntity.id, forecast.toSlotEntities(cityEntity.id))
    }

    override suspend fun refreshIfStale(cityId: Int): Resource<Boolean> = tryResourceSuspend {
        var hasRefreshed = false
        val city = localDataSource.getCityById(cityId) ?: throw CityNotFoundException()
        val cachedCurrentWeather = localDataSource.getCurrentWeather(cityId)
        if (cachedCurrentWeather == null || CacheUtils.isStale(
                cachedCurrentWeather.fetchedAt, CacheUtils.CURRENT_WEATHER_TTL
            )
        ) {
            refreshCurrentWeather(cityEntity = city)
            hasRefreshed = true
        }
        val forecast = localDataSource.getLastForecastSlot(cityId)
        if (forecast == null || CacheUtils.isStale(
                forecast.fetchedAt, CacheUtils.FORECAST_TTL
            )
        ) {
            refreshForecast(cityEntity = city)
            hasRefreshed = true
        }
        hasRefreshed
    }

    override suspend fun refreshWithDebounce(cityId: Int, debounceMillis: Long): Resource<Boolean> =
        tryResourceSuspend {
            val city = localDataSource.getCityById(cityId) ?: throw CityNotFoundException()
            val cachedCurrentWeather = localDataSource.getCurrentWeather(cityId)
            if (cachedCurrentWeather == null || CacheUtils.isStale(
                    cachedCurrentWeather.fetchedAt, debounceMillis
                )
            ) {
                refreshWeather(city)
                return@tryResourceSuspend true
            }
            val forecast = localDataSource.getLastForecastSlot(cityId)
            if (forecast == null || CacheUtils.isStale(
                    forecast.fetchedAt, debounceMillis
                )
            ) {
                refreshWeather(city)
                return@tryResourceSuspend true
            }
            return@tryResourceSuspend false
        }

    override suspend fun refreshAllCache(): Resource<Unit> = tryResourceSuspend {
        val cities = localDataSource.getAllCities()
        cities.forEach { city ->
            refreshWeather(city)
        }
    }

    override suspend fun refreshAllIfStale(): Resource<Unit> = tryResourceSuspend {
        val cities = localDataSource.getAllCities()
        cities.forEach { city ->
            refreshIfStale(city.id)
        }
    }

    override fun observeAllAlerts(): Flow<List<AlertCityDetails>> =
        localDataSource.observeAllAlerts().map { alerts -> alerts.map { it.toDomainModel() } }

    override fun observeAllActiveAlerts(): Flow<List<AlertCityDetails>> =
        localDataSource.observeAllActiveAlerts().map { alerts -> alerts.map { it.toDomainModel() } }

    override suspend fun upsertAlert(alert: Alert): Resource<Unit> = tryResourceSuspend {
        val addedAlert = localDataSource.upsertAlert(alert.toEntity()).toDomainModel()
        if (alert.isEnabled) {
            schedulerManager.schedule(addedAlert)
        }
    }

    override suspend fun updateAlertEnabled(
        alertId: Int, enabled: Boolean
    ): Resource<Unit> = tryResourceSuspend {
        val alert =
            localDataSource.getAlertById(alertId)?.toDomainModel() ?: throw AlertNotFoundException()
        localDataSource.updateAlertEnabled(alertId, enabled)
        if (enabled) schedulerManager.schedule(alert) else schedulerManager.cancel(alert)
    }

    override suspend fun deleteAlert(alertId: Int): Resource<Unit> = tryResourceSuspend {
        val alert =
            localDataSource.getAlertById(alertId)?.toDomainModel() ?: throw AlertNotFoundException()
        schedulerManager.cancel(alert)
        localDataSource.deleteAlert(alertId)
    }

    override suspend fun getAlertById(alertId: Int): Resource<Alert> = tryResourceSuspend {
        localDataSource.getAlertById(alertId)?.toDomainModel() ?: throw AlertNotFoundException()
    }

    override suspend fun rescheduleAlert(alertId: Int): Resource<Unit> = tryResourceSuspend {
        val existingAlert =
            localDataSource.getAlertById(alertId)?.toDomainModel() ?: throw AlertNotFoundException()
        if (existingAlert.isEnabled) {
            schedulerManager.cancel(existingAlert)
            schedulerManager.schedule(existingAlert)
        }
    }

    override suspend fun rescheduleAllAlerts(): Resource<Unit> = tryResourceSuspend {
        val alerts = localDataSource.getAllActiveAlerts().map { it.toDomainModel().alert }
        alerts.forEach { alert ->
            if (alert.isEnabled) {
                schedulerManager.cancel(alert)
                schedulerManager.schedule(alert)
            }
        }
    }

    override suspend fun updateAllCurrentWeatherOnOffline(): Resource<Unit> = tryResourceSuspend {
        val cities = localDataSource.getAllCities()
        cities.forEach { city ->
            val cachedCurrentWeather = localDataSource.getCurrentWeather(city.id)
            val cachedForecast = localDataSource.getLastForecastSlot(city.id)
            if (cachedCurrentWeather == null || cachedForecast == null) return@forEach
            if (CacheUtils.shouldReplaceCurrentWeather(
                    cachedCurrentWeather.datetime,
                    cachedForecast.datetime
                )
            ) {
                localDataSource.upsertCurrentWeather(
                    cachedForecast.toCurrentWeatherEntity(
                        cachedCurrentWeather.sunrise,
                        cachedCurrentWeather.sunset
                    )
                )
                localDataSource.deleteForecastSlot(cachedForecast.id)
            }
        }
    }
}