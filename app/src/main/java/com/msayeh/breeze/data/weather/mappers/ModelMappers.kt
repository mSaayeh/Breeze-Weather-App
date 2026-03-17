package com.msayeh.breeze.data.weather.mappers

import com.msayeh.breeze.data.weather.local.entities.AlertEntity
import com.msayeh.breeze.domain.model.Alert
import java.time.LocalDate
import java.time.ZoneId

fun Alert.toEntity() = AlertEntity(
    cityId = cityId,
    alertTime = time.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toEpochSecond(),
    alertType = type.code,
    isEnabled = isEnabled,
)