package com.msayeh.breeze.domain.model

import androidx.annotation.DrawableRes
import com.msayeh.breeze.R

data class WeatherCondition(
    val title: String,
    val iconCode: String,
) {
    val iconRes: Int
        get() = getDrawableFromIconCode()
    @DrawableRes
    private fun getDrawableFromIconCode(): Int = when (iconCode) {
        "01d" -> R.drawable.sunny
        "01n" -> R.drawable.moon
        "02d" -> R.drawable.cloudy_d
        "02n" -> R.drawable.cloudy_n
        "03d" -> R.drawable.heavy_clouds
        "03n" -> R.drawable.heavy_clouds
        "04d" -> R.drawable.heavy_clouds
        "04n" -> R.drawable.heavy_clouds
        "09d" -> R.drawable.heavy_rain
        "09n" -> R.drawable.heavy_rain
        "10d" -> R.drawable.rain_d
        "10n" -> R.drawable.rain_n
        "11d" -> R.drawable.thunder
        "11n" -> R.drawable.thunder
        "13d" -> R.drawable.snow
        "13n" -> R.drawable.snow
        "50d" -> R.drawable.mist
        "50n" -> R.drawable.mist
        else -> R.drawable.ic_launcher_foreground
    }
}