package com.msayeh.breeze.data.weather.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.data.weather.local.entities.ForecastSlotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM current_weather WHERE cityId = :cityId")
    fun observeCurrentWeather(cityId: Int): Flow<CurrentWeatherEntity?>

    @Query("SELECT * FROM current_weather WHERE cityId = :cityId")
    suspend fun getCurrentWeather(cityId: Int): CurrentWeatherEntity?

    @Upsert
    suspend fun upsertCurrentWeather(currentWeather: CurrentWeatherEntity)

    @Query("DELETE FROM current_weather WHERE cityId = :cityId")
    suspend fun deleteCurrentWeather(cityId: Int)

    @Query("SELECT * FROM forecast_slots WHERE cityId = :cityId ORDER BY datetime ASC")
    fun observeForecastSlots(cityId: Int): Flow<List<ForecastSlotEntity>>

    @Query("SELECT * FROM forecast_slots WHERE cityId = :cityId ORDER BY datetime ASC LIMIT 1")
    suspend fun getLastForecastSlot(cityId: Int): ForecastSlotEntity?

    @Query("DELETE FROM forecast_slots WHERE id = :slotId")
    suspend fun deleteForecastSlot(slotId: Long)

    @Query("SELECT * FROM forecast_slots WHERE cityId = :cityId AND datetime >= :fromTime ORDER BY datetime ASC")
    fun observeForecastSlotsFrom(cityId: Int, fromTime: Long): Flow<List<ForecastSlotEntity>>

    @Upsert
    suspend fun upsertForecastSlots(forecastDays: List<ForecastSlotEntity>)

    @Query("DELETE FROM forecast_slots WHERE cityId = :cityId")
    suspend fun deleteForecastSlots(cityId: Int)

    @Transaction
    suspend fun replaceForecast(cityId: Int, forecastDays: List<ForecastSlotEntity>) {
        deleteForecastSlots(cityId)
        upsertForecastSlots(forecastDays)
    }
}