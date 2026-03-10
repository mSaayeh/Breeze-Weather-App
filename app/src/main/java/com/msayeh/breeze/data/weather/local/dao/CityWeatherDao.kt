package com.msayeh.breeze.data.weather.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.msayeh.breeze.data.weather.local.entities.CityWithWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface CityWeatherDao {
    @Transaction
    @Query("SELECT * FROM cities ORDER BY sortOrder ASC")
    fun observeAllCitiesWithWeather(): Flow<List<CityWithWeather>>

    @Transaction
    @Query("SELECT * FROM cities WHERE id = :cityId")
    fun observeCityWithWeatherById(cityId: Int): Flow<CityWithWeather?>
}