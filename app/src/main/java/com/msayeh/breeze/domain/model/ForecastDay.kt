package com.msayeh.breeze.domain.model

import java.time.LocalDate

data class ForecastDay(
    val date: LocalDate,
    val slots: List<ForecastSlot>,
    val representative: ForecastSlot,
    val temperatureRange: TemperatureRange,
)
