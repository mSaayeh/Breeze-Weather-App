package com.msayeh.breeze.domain.repository

import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Wind
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun isFirstTimeLaunch(): Boolean

    fun getChosenCityIdFlow(): Flow<Int?>
    suspend fun saveChosenCityId(cityId: Int)

    fun isDarkThemeEnabled(): Flow<Boolean?>
    suspend fun saveDarkTheme(isDarkThemeEnabled: Boolean)

    fun getLanguage(): Flow<Int?>
    suspend fun saveLanguage(language: Int)

    fun getTempUnit(): Flow<Temperature.Unit?>
    suspend fun saveTempUnit(unit: Temperature.Unit)

    fun getSpeedUnit(): Flow<Wind.Unit?>
    suspend fun saveSpeedUnit(unit: Wind.Unit)

    suspend fun clearPreferences()
}