package com.msayeh.breeze.data.weather.mappers

import com.msayeh.breeze.data.weather.remote.dto.WeatherResponseDto
import com.msayeh.breeze.domain.model.Weather

fun WeatherResponseDto.toDomainModel(): Weather {
    return Weather(
        cityId = id,
        cityName = name,
        weatherName = weather[0].main,
        iconCode = weather[0].icon,
        temperature = main.temp,
        feelsLike = main.feelsLike,
        minTemp = main.tempMin,
        maxTemp = main.tempMax,
        windSpeed = wind.speed,
        windDeg = wind.deg,
        pressure = main.pressure,
        humidity = main.humidity,
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        timezone = timezone
    )
}