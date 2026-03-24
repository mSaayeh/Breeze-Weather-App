package com.msayeh.breeze.domain.model

import androidx.annotation.StringRes
import com.msayeh.breeze.R
import kotlin.math.round
import kotlin.math.roundToInt

data class Temperature(
    val celsius: Double,
) {
    val fahrenheit: Double
        get() = (celsius * 9 / 5) + 32
    val kelvin: Double
        get() = celsius + 273.15

    enum class Unit(val code: Int, val symbol: String, @param:StringRes val nameResId: Int) {
        CELSIUS(1001, "°", R.string.celsius), FAHRENHEIT(1002, "°", R.string.fahrenheit), KELVIN(
            1003,
            "",
            R.string.kelvin
        );

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
        "%s%d%s".format(
            if (getWithUnit(unit).roundToInt() == 0) "~" else "",
            getWithUnit(unit).roundToInt(),
            if (includeSymbol) unit.symbol else ""
        )
}
