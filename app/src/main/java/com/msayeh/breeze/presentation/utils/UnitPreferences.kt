package com.msayeh.breeze.presentation.utils

import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Wind

data class UnitPreferences(
    val tempUnit: Temperature.Unit,
    val speedUnit: Wind.Unit,
)
