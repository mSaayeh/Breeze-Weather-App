package com.msayeh.breeze.domain.model

import androidx.annotation.DrawableRes

data class WeatherCondition(
    val title: String,
    val iconCode: String,
    @param:DrawableRes val iconDrawableRes: Int
)