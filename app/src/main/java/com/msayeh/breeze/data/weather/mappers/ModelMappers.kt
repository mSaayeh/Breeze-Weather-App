package com.msayeh.breeze.data.weather.mappers

import com.msayeh.breeze.data.weather.local.entities.AlertEntity
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.AlertType
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

fun Alert.toEntity() = AlertEntity(
    cityId = cityId,
    alertTime = alertTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toEpochSecond(),
    alertType = alertType.code,
    isEnabled = isEnabled,
)