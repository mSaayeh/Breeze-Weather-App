package com.msayeh.breeze.domain.model

data class TemperatureRange(
    val min: Temperature,
    val max: Temperature,
) {
    fun format(unit: Temperature.Unit): String = "%.0f%s-%.0f%s".format(
        min.getWithUnit(unit),
        unit.symbol,
        max.getWithUnit(unit),
        unit.symbol
    )
}