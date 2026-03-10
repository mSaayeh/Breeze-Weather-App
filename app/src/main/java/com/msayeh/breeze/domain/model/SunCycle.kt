package com.msayeh.breeze.domain.model

import java.time.LocalTime

data class SunCycle(
    val sunrise: LocalTime,
    val sunset: LocalTime,
)
