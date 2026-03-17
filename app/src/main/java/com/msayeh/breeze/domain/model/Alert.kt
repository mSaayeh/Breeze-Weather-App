package com.msayeh.breeze.domain.model

import java.time.LocalTime

data class Alert(
    val id: Int = 0,
    val cityId: Int,
    val isEnabled: Boolean,
    val time: LocalTime,
    val type: AlertType,
)

enum class AlertType(val code: Int) {
    NOTIFICATION(8001),
    ALARM(8002);

    companion object {
        fun fromCode(code: Int): AlertType = entries.first { it.code == code }
    }
}