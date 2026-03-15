package com.msayeh.breeze.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.msayeh.breeze.R

data class Wind(
    val speedMs: Double,
    val deg: Int,
) {
    val speedKmHr: Double
        get() = speedMs * 3.6
    val speedMph: Double
        get() = speedMs * 2.237

    enum class Unit(val code: Int, val symbolResId: Int) {
        METRIC_MS(2001, R.string.m_s),
        METRIC_KMH(2002, R.string.km_h),
        IMPERIAL(2003, R.string.mph);

        companion object {
            fun fromCode(code: Int): Unit = entries.first { it.code == code }
        }
    }

    fun getWithUnit(unit: Unit): Double = when (unit) {
        Unit.METRIC_MS -> speedMs
        Unit.METRIC_KMH -> speedKmHr
        Unit.IMPERIAL -> speedMph
    }

    @Composable
    fun formatSpeed(unit: Unit, includeUnit: Boolean = true): String =
        "%.0f %s".format(getWithUnit(unit), if (includeUnit) stringResource(unit.symbolResId) else "")
}
