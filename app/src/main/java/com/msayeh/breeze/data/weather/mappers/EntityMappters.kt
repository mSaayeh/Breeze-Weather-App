package com.msayeh.breeze.data.weather.mappers

import androidx.annotation.DrawableRes
import com.msayeh.breeze.R
import com.msayeh.breeze.data.utils.Constants
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import com.msayeh.breeze.data.weather.local.entities.CityWithWeather
import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.data.weather.local.entities.ForecastSlotEntity
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.CityWeatherDetails
import com.msayeh.breeze.domain.model.Coordinates
import com.msayeh.breeze.domain.model.ForecastSlot
import com.msayeh.breeze.domain.model.SunCycle
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Weather
import com.msayeh.breeze.domain.model.WeatherCondition
import com.msayeh.breeze.domain.model.Wind
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.seconds

fun CityEntity.toDomainModel() = City(
    id = id,
    name = name,
    country = country,
    coordinates = Coordinates(latitude, longitude),
    isCurrentLocation = id == -1,
    sortOrder = sortOrder,
)

fun CurrentWeatherEntity.toDomainModel() = Weather(
    cityId = cityId,
    temperature = Temperature(tempCelsius),
    feelsLike = Temperature(feelsLikeCelsius),
    humidity = humidity,
    wind = Wind(windSpeedMpS, windDeg),
    condition = WeatherCondition(
        title = weatherName,
        iconCode = iconCode,
        iconDrawableRes = getDrawableFromIconCode(iconCode),
    ),
    pressure = pressure,
    sunCycle = SunCycle(
        sunrise = Instant.ofEpochSecond(sunrise)
            .atZone(ZoneId.systemDefault())
            .toLocalTime(),
        sunset = Instant.ofEpochSecond(sunset)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
    ),
    fetchedAt = fetchedAt
)

fun ForecastSlotEntity.toDomainModel() = ForecastSlot(
    datetime = Instant.ofEpochSecond(datetime).atZone(ZoneId.systemDefault()).toLocalDateTime(),
    weather = Weather(
        cityId = cityId,
        temperature = Temperature(tempCelsius),
        feelsLike = Temperature(feelsLikeCelsius),
        humidity = humidity,
        wind = Wind(windSpeedMpS, windDeg),
        condition = WeatherCondition(
            iconCode = iconCode,
            title = weatherName,
            iconDrawableRes = getDrawableFromIconCode(iconCode),
        ),
        pressure = pressure,
        sunCycle = null,
        fetchedAt = fetchedAt
    )
)

fun CityWithWeather.toDomainModel() = CityWeatherDetails(
    city = city.toDomainModel(),
    currentWeather = currentWeather?.toDomainModel(),
    forecastSlots = forecastSlots.map { it.toDomainModel();  }
)
@DrawableRes
fun getDrawableFromIconCode(iconCode: String): Int = when (iconCode) {
    else -> R.drawable.ic_launcher_foreground
}