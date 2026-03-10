package com.msayeh.breeze.domain.model

data class Temperature(
    val celsius: Double,
) {
    val fahrenheit: Double
        get() = (celsius * 9 / 5) + 32
    val kelvin: Double
        get() = celsius + 273.15

    enum class Unit(val code: String) {
        CELSIUS("C"), FAHRENHEIT("F"), KELVIN("K");

        companion object {
            fun fromCode(code: String): Unit = entries.first { it.code == code }
        }
    }

    fun getWithUnit(unit: Unit): Double = when (unit) {
        Unit.CELSIUS -> celsius
        Unit.FAHRENHEIT -> fahrenheit
        Unit.KELVIN -> kelvin
    }
}
