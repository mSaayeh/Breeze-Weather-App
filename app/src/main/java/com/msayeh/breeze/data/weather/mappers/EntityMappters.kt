package com.msayeh.breeze.data.weather.mappers

import androidx.annotation.StringRes
import com.msayeh.breeze.R
import com.msayeh.breeze.data.weather.local.entities.AlertEntity
import com.msayeh.breeze.data.weather.local.entities.AlertWithCity
import com.msayeh.breeze.data.weather.local.entities.CityEntity
import com.msayeh.breeze.data.weather.local.entities.CityWithWeather
import com.msayeh.breeze.data.weather.local.entities.CurrentWeatherEntity
import com.msayeh.breeze.data.weather.local.entities.ForecastSlotEntity
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.AlertCityDetails
import com.msayeh.breeze.domain.model.AlertType
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
import java.time.ZoneId

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
        nameResId = getConditionStringResId(weatherConditionId),
        iconCode = iconCode,
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
            nameResId = getConditionStringResId(weatherConditionId),
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

fun AlertEntity.toDomainModel() = Alert(
    id = id,
    cityId = cityId,
    isEnabled = isEnabled,
    time = Instant.ofEpochSecond(alertTime).atZone(ZoneId.systemDefault()).toLocalTime(),
    type = AlertType.fromCode(alertType),
)

fun AlertWithCity.toDomainModel() = AlertCityDetails(
    city = city.toDomainModel(),
    alert = alert.toDomainModel()
)

@StringRes
fun getConditionStringResId(conditionId: Int): Int {
    return when (conditionId) {
        in 200..299 -> R.string.thunderstorm
        in 300..399 -> R.string.drizzle
        in 500..599 -> R.string.rain
        in 600..699 -> R.string.snow
        in 701..710 -> R.string.mist
        in 711..720 -> R.string.smoke
        in 721..730 -> R.string.haze
        in 731..740 -> R.string.dust
        in 741..750 -> R.string.fog
        in 751..760 -> R.string.sand
        761 -> R.string.dust
        762 -> R.string.ash
        in 771..780 -> R.string.squall
        in 781..799 -> R.string.tornado
        800 -> R.string.clear
        in 801..899 -> R.string.clouds
        else -> R.string.unknown
    }
}