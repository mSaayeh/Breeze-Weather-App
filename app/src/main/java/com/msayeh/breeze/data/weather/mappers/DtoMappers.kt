package com.msayeh.breeze.data.weather.mappers

import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.data.weather.local.entities.ForecastSlotEntity
import com.msayeh.breeze.data.weather.remote.dto.ForecastResponseDto
import com.msayeh.breeze.data.weather.remote.dto.ForecastSlotDto
import com.msayeh.breeze.data.weather.remote.dto.GeoCityDto
import com.msayeh.breeze.data.weather.remote.dto.WeatherResponseDto
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.Coordinates

fun WeatherResponseDto.toEntity(cityId: Int): CurrentWeatherEntity = CurrentWeatherEntity(
    cityId = cityId,
    weatherConditionId = weather[0].id,
    tempCelsius = main.temp,
    feelsLikeCelsius = main.feelsLike,
    minTempCelsius = main.tempMin,
    maxTempCelsius = main.tempMax,
    windSpeedMpS = wind.speed,
    windDeg = wind.deg,
    humidity = main.humidity,
    pressure = main.pressure,
    iconCode = weather[0].icon,
    sunrise = sys.sunrise ?: 0,
    sunset = sys.sunset ?: 0,
    datetime = datetime,
    fetchedAt = fetchedAt,
)

fun ForecastSlotDto.toEntity(cityId: Int, fetchedAt: Long) = ForecastSlotEntity(
    cityId = cityId,
    weatherConditionId = weather.first().id,
    datetime = datetime,
    tempCelsius = main.temp,
    feelsLikeCelsius = main.feelsLike,
    minTempCelsius = main.tempMin,
    maxTempCelsius = main.tempMax,
    windSpeedMpS = wind.speed,
    windDeg = wind.deg,
    humidity = main.humidity,
    pressure = main.pressure,
    iconCode = weather.first().icon,
    fetchedAt = fetchedAt,
)

fun ForecastResponseDto.toSlotEntities(cityId: Int): List<ForecastSlotEntity> =
    forecastsList.map { it.toEntity(cityId, fetchedAt) }

fun GeoCityDto.toDomainModel(isCurrentLocation: Boolean): City = City(
    id = -1,
    name = name,
    country = country,
    coordinates = Coordinates(lat, lon),
    isCurrentLocation = isCurrentLocation,
    sortOrder = -1
)