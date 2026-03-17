package com.msayeh.breeze.presentation.screens.addalerts.viewmodel

import com.msayeh.breeze.domain.model.AlertType
import com.msayeh.breeze.domain.model.City
import java.time.LocalTime

data class AddAlertState(
    val cities: List<City> = emptyList(),
    val selectedCityId: Int? = null,
    val time: LocalTime = defaultAlertTime(),
    val type: AlertType = AlertType.NOTIFICATION,
    val isSaving: Boolean = false,
) {
    val isSaveEnabled: Boolean = selectedCityId != null && !isSaving && cities.isNotEmpty()
}

private fun defaultAlertTime(now: LocalTime = LocalTime.now()): LocalTime {
    // Default to the next hour (e.g. 10:xx -> 11:00), which works well with daily scheduling.
    return now
        .plusHours(1)
        .withMinute(0)
        .withSecond(0)
        .withNano(0)
}

