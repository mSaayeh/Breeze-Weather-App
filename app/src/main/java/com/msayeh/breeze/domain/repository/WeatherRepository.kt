package com.msayeh.breeze.domain.repository

import com.msayeh.breeze.data.utils.CacheUtils
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.AlertCityDetails
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.CityWeatherDetails
import com.msayeh.breeze.domain.model.Coordinates
import com.msayeh.breeze.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface WeatherRepository {
    fun observeAllCities(): Flow<List<City>>
    fun observeCityWithWeather(cityId: Int): Flow<CityWeatherDetails?>
    fun observeAllCitiesWithWeather(): Flow<List<CityWeatherDetails>>
    suspend fun getCitySuggestions(query: String): Resource<List<City>>
    suspend fun getCityByCoordinates(coordinates: Coordinates, isCurrentLocation: Boolean = false): Resource<City>
    suspend fun addCityToFavorites(city: City): Resource<City>
    suspend fun removeCityFromFavorites(cityId: Int): Resource<Unit>
    suspend fun refreshWeather(cityId: Int): Resource<Unit>
    suspend fun refreshCurrentWeather(cityId: Int): Resource<Unit>
    suspend fun refreshIfStale(cityId: Int): Resource<Boolean>
    suspend fun refreshWithDebounce(cityId: Int, debounceMillis: Long = CacheUtils.DEFAULT_REFRESH_DEBOUNCE_TIME): Resource<Boolean>
    suspend fun refreshAllCache(): Resource<Unit>
    suspend fun refreshAllIfStale(): Resource<Unit>
    fun observeAllAlerts(): Flow<List<AlertCityDetails>>
    fun observeAllActiveAlerts(): Flow<List<AlertCityDetails>>
    suspend fun upsertAlert(alert: Alert): Resource<Unit>
    suspend fun updateAlertEnabled(alertId: Int, enabled: Boolean): Resource<Unit>
    suspend fun deleteAlert(alertId: Int): Resource<Unit>
    suspend fun getAlertById(alertId: Int): Resource<Alert>
    suspend fun rescheduleAlert(alertId: Int): Resource<Unit>
    suspend fun rescheduleAllAlerts(): Resource<Unit>
    suspend fun updateCurrentWeatherOnOffline(cityId: Int): Resource<Unit>
    suspend fun updateAllCurrentWeatherOnOffline(): Resource<Unit>
}