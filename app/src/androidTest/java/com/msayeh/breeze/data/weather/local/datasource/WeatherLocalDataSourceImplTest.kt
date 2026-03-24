package com.msayeh.breeze.data.weather.local.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.msayeh.breeze.data.weather.local.dao.CityAlertDao
import com.msayeh.breeze.data.weather.local.dao.CityDao
import com.msayeh.breeze.data.weather.local.dao.CityWeatherDao
import com.msayeh.breeze.data.weather.local.dao.WeatherDao
import com.msayeh.breeze.data.weather.local.database.WeatherDatabase
import com.msayeh.breeze.data.weather.local.entities.AlertEntity
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.domain.exception.CityNotFoundException
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.Long


class WeatherLocalDataSourceImplTest {
    private lateinit var dataSource: WeatherLocalDataSourceImpl

    private lateinit var weatherDao: WeatherDao
    private lateinit var cityDao: CityDao
    private lateinit var cityWeatherDao: CityWeatherDao
    private lateinit var cityAlertDao: CityAlertDao

    private lateinit var db: WeatherDatabase

    val currentWeatherEntity = CurrentWeatherEntity(
        cityId = 5,
        weatherConditionId = 201,
        tempCelsius = 15.2,
        feelsLikeCelsius = 16.2,
        minTempCelsius = 10.5,
        maxTempCelsius = 18.2,
        windSpeedMpS = 15.0,
        windDeg = 15,
        humidity = 100,
        pressure = 1000,
        iconCode = "TODO()",
        sunrise = 1002020L,
        sunset = 102020020L,
        datetime = 15561561L,
        fetchedAt = 1518917,
    )

    val cityEntity = CityEntity(
        10,
        "Cairo",
        "EG",
        15.2,
        16.5,
        1,
        3,
    )

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        weatherDao = db.weatherDao()
        cityDao = db.cityDao()
        cityWeatherDao = db.cityWeatherDao()
        cityAlertDao = db.cityAlertDao()

        dataSource = WeatherLocalDataSourceImpl(weatherDao, cityDao, cityWeatherDao, cityAlertDao)
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun upsertCurrentWeather_cityIdNotInDB_ignoresException() = runTest {
        try {
            // When
            dataSource.upsertCurrentWeather(currentWeatherEntity)
            // Then
            // Make sure this line is reached
            assertThat(true, `is`(true))
        } catch (e: Exception) {
            assertThat(true, `is`(false))
        }
    }

    @Test
    fun upsertCity_cityIdNotInDB_getsCitySuccessfully() = runTest {
        try {
            // When
            dataSource.upsertCity(cityEntity)
            // Then
            val actualCity = dataSource.getCityById(cityEntity.id)
            assertThat(actualCity, `is`(cityEntity))
        } catch (e: Exception) {
            assertThat(true, `is`(false))
        }
    }

    @Test
    fun deleteAlert_alertExistsInDB_returnsOneAndAlertIsGone() = runTest {
        try {
            // Given
            dataSource.upsertCity(cityEntity)
            val alert = AlertEntity(
                id = 1,
                cityId = cityEntity.id,
                alertTime = System.currentTimeMillis(),
                alertType = 1,
                isEnabled = true
            )
            dataSource.upsertAlert(alert)

            // When
            val deletedCount = dataSource.deleteAlert(alert.id)

            // Then
            assertThat(deletedCount, `is`(1))
        } catch (e: Exception) {
            assertThat(true, `is`(false))
        }
    }
}