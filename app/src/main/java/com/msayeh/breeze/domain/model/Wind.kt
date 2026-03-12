package com.msayeh.breeze.domain.model

data class Wind(
    val speedMs: Double,
    val deg: Int,
) {
    val speedKmHr: Double
        get() = speedMs * 3.6
    val speedMph: Double
        get() = speedMs * 2.237

    enum class Unit(val code: Int) {
        METRIC_MS(2001), METRIC_KMH(2002), IMPERIAL(2003);
        companion object {
            fun fromCode(code: Int): Unit = entries.first { it.code == code }
        }
    }

    fun getWithUnit(unit: Unit): Double = when (unit) {
        Unit.METRIC_MS -> speedMs
        Unit.METRIC_KMH -> speedKmHr
        Unit.IMPERIAL -> speedMph
    }
}
