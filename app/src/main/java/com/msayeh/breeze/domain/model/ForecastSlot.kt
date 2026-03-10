package com.msayeh.breeze.domain.model

import java.time.LocalDateTime
import kotlin.math.abs

data class ForecastSlot(
    val datetime: LocalDateTime,
    val weather: Weather,
)

fun List<ForecastSlot>.groupByDay(): List<ForecastDay> =
    groupBy {
        it.datetime.toLocalDate()
    }.map { (date, slots) ->
        ForecastDay(
            date = date,
            slots = slots,
            representative = slots.minByOrNull { abs(it.datetime.hour - 12) } ?: slots.first(),
            temperatureRange = TemperatureRange(
                Temperature(slots.minOf { it.weather.temperature.celsius }),
                Temperature(slots.maxOf { it.weather.temperature.celsius })
            )
        )
    }.sortedBy { it.date }