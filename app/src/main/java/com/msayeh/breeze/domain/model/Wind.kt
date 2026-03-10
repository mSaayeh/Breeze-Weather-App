package com.msayeh.breeze.domain.model

data class Wind(
    val speedMs: Double,
    val deg: Int,
) {
    val speedKmHr: Double
        get() = speedMs * 3.6
    val speedMph: Double
        get() = speedMs * 2.237

    enum class Unit(val code: String) {
        METRIC_MS("ms"), METRIC_KMH("kmh"), IMPERIAL("mph");
        companion object {
            fun fromCode(code: String): Unit = entries.first { it.code == code }
        }
    }

    fun getWithUnit(unit: Unit): Double = when (unit) {
        Unit.METRIC_MS -> speedMs
        Unit.METRIC_KMH -> speedKmHr
        Unit.IMPERIAL -> speedMph
    }
}
