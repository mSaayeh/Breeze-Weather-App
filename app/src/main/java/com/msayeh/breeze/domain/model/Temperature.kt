package com.msayeh.breeze.domain.model

import androidx.annotation.StringRes
import com.msayeh.breeze.R

data class Temperature(
    val celsius: Double,
) {
    val fahrenheit: Double
        get() = (celsius * 9 / 5) + 32
    val kelvin: Double
        get() = celsius + 273.15

    enum class Unit(val code: Int, val symbol: String, @param:StringRes val nameResId: Int) {
        CELSIUS(1001, "°", R.string.celsius), FAHRENHEIT(1002, "°", R.string.fahrenheit), KELVIN(1003, "", R.string.kelvin);

        companion object {
            fun fromCode(code: Int): Unit = entries.first { it.code == code }
        }
    }

    fun getWithUnit(unit: Unit): Double = when (unit) {
        Unit.CELSIUS -> celsius
        Unit.FAHRENHEIT -> fahrenheit
        Unit.KELVIN -> kelvin
    }

    fun format(unit: Unit, includeSymbol: Boolean = true): String =
        "%.0f%s".format(getWithUnit(unit), if (includeSymbol) unit.symbol else "")
}
