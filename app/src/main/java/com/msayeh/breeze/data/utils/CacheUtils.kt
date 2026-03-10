package com.msayeh.breeze.data.utils

object CacheUtils {
    const val CURRENT_WEATHER_TTL = 30 * 60 * 1000L
    const val FORECAST_TTL = 3 * 60 * 60 * 1000L

    fun isStale(fetchedAt: Long, ttl: Long): Boolean =
        System.currentTimeMillis() - fetchedAt > ttl
}