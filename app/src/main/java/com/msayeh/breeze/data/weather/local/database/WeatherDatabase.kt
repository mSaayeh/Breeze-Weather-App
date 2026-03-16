package com.msayeh.breeze.data.weather.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msayeh.breeze.data.weather.local.dao.CityAlertDao
import com.msayeh.breeze.data.weather.local.dao.CityDao
import com.msayeh.breeze.data.weather.local.dao.CityWeatherDao
import com.msayeh.breeze.data.weather.local.dao.WeatherDao
import com.msayeh.breeze.data.weather.local.entities.AlertEntity
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.data.weather.local.entities.ForecastSlotEntity

@Database(
    entities = [
        CityEntity::class,
        CurrentWeatherEntity::class,
        ForecastSlotEntity::class,
        AlertEntity::class,
    ],
    version = 3,
    exportSchema = true
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun cityWeatherDao(): CityWeatherDao
    abstract fun cityDao(): CityDao
    abstract fun weatherDao(): WeatherDao
    abstract fun cityAlertDao(): CityAlertDao

    companion object {
        const val DATABASE_NAME = "weather_db"
    }
}