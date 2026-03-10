package com.msayeh.breeze.data.weather.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY sortOrder ASC")
    fun observeAllCities(): Flow<List<CityEntity>>

    @Query("SELECT * FROM cities ORDER BY sortOrder ASC")
    suspend fun getAllCities(): List<CityEntity>

    @Query("SELECT * FROM cities WHERE id = :cityId")
    fun observeCityById(cityId: Int): Flow<CityEntity?>

    @Query("SELECT * FROM cities WHERE id = :cityId")
    suspend fun getCityById(cityId: Int): CityEntity?

    @Upsert
    suspend fun upsertCity(city: CityEntity)

    @Upsert
    suspend fun upsertAllCities(cities: List<CityEntity>)

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("DELETE FROM cities WHERE id = :cityId")
    suspend fun deleteCityById(cityId: Int)

    @Query("DELETE FROM cities")
    suspend fun clearCities()
}
