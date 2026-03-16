package com.msayeh.breeze.domain.repository

import com.msayeh.breeze.data.utils.AppLanguage
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Wind
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun checkAndMarkFirstLaunch(): Boolean

    fun getChosenCityIdFlow(): Flow<Int?>
    suspend fun saveChosenCityId(cityId: Int)

    fun isDarkThemeEnabledFlow(): Flow<Boolean?>
    suspend fun saveDarkTheme(isDarkThemeEnabled: Boolean)

    fun getLanguageFlow(): Flow<AppLanguage>
    suspend fun getLanguage(): AppLanguage
    suspend fun saveLanguage(appLanguage: AppLanguage)

    fun getTempUnitFlow(): Flow<Temperature.Unit>
    suspend fun saveTempUnit(unit: Temperature.Unit)

    fun getSpeedUnitFlow(): Flow<Wind.Unit>
    suspend fun saveSpeedUnit(unit: Wind.Unit)

    suspend fun clearPreferences()
}