package com.msayeh.breeze.data.weather.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.msayeh.breeze.data.weather.local.database.WeatherDatabase
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.data.weather.local.entities.ForecastSlotEntity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var db: WeatherDatabase
    private lateinit var weatherDao: WeatherDao
    private lateinit var cityDao: CityDao

    private val cityEntity = CityEntity(
        id = 1,
        name = "Cairo",
        country = "EG",
        latitude = 30.0,
        longitude = 31.0,
        sortOrder = 0
    )

    private val currentWeatherEntity = CurrentWeatherEntity(
        cityId = 1,
        weatherConditionId = 201,
        tempCelsius = 25.0,
        feelsLikeCelsius = 24.0,
        minTempCelsius = 20.0,
        maxTempCelsius = 30.0,
        windSpeedMpS = 5.0,
        windDeg = 180,
        humidity = 60,
        pressure = 1013,
        iconCode = "01d",
        sunrise = 1000L,
        sunset = 2000L,
        datetime = 1500L,
        fetchedAt = System.currentTimeMillis()
    )

    private val forecastSlotEntity = ForecastSlotEntity(
        cityId = 1,
        weatherConditionId = 201,
        datetime = 1000L,
        tempCelsius = 22.0,
        feelsLikeCelsius = 21.0,
        minTempCelsius = 18.0,
        maxTempCelsius = 26.0,
        windSpeedMpS = 3.0,
        windDeg = 90,
        humidity = 70,
        pressure = 1010,
        iconCode = "02d",
        fetchedAt = System.currentTimeMillis()
    )

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        weatherDao = db.weatherDao()
        cityDao = db.cityDao()

        runBlocking {
            cityDao.upsertCity(cityEntity)
        }
    }

    @After
    fun teardown() {
        db.close()
    }


    @Test
    fun getCurrentWeather_weatherExistsInDB_returnsCorrectWeather() = runTest {
        // Given
        weatherDao.upsertCurrentWeather(currentWeatherEntity)

        // When
        val result = weatherDao.getCurrentWeather(cityEntity.id)

        // Then
        assertThat(result, `is`(currentWeatherEntity))
    }

    @Test
    fun getCurrentWeather_weatherNotInDB_returnsNull() = runTest {
        // When
        val result = weatherDao.getCurrentWeather(cityEntity.id)

        // Then
        assertThat(result, `is`(nullValue()))
    }



    @Test
    fun deleteCurrentWeather_weatherExistsInDB_weatherIsGone() = runTest {
        // Given
        weatherDao.upsertCurrentWeather(currentWeatherEntity)

        // When
        weatherDao.deleteCurrentWeather(cityEntity.id)

        // Then
        val result = weatherDao.getCurrentWeather(cityEntity.id)
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun deleteCurrentWeather_weatherNotInDB_doesNothing() = runTest {
        weatherDao.deleteCurrentWeather(999)
        val result = weatherDao.getCurrentWeather(999)
        assertThat(result, `is`(nullValue()))
    }



    @Test
    fun getLastForecastSlot_slotsExistInDB_returnsEarliestSlot() = runTest {
        // Given
        val slot1 = forecastSlotEntity.copy(datetime = 1000L)
        val slot2 = forecastSlotEntity.copy(datetime = 2000L)
        val slot3 = forecastSlotEntity.copy(datetime = 3000L)
        weatherDao.upsertForecastSlots(listOf(slot1, slot2, slot3))

        // When
        val result = weatherDao.getLastForecastSlot(cityEntity.id)

        // Then
        assertThat(result?.datetime, `is`(1000L))
    }

    @Test
    fun getLastForecastSlot_noSlotsInDB_returnsNull() = runTest {
        // When
        val result = weatherDao.getLastForecastSlot(cityEntity.id)

        // Then
        assertThat(result, `is`(nullValue()))
    }
}