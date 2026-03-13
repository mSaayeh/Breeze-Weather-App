package com.msayeh.breeze.data.preferences.datasource

import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    fun chosenCityIdFlow(): Flow<Int?>

    suspend fun isFirstTimeUser(): Boolean

    suspend fun saveFirstTimeUser(isFirstTimeUser: Boolean)
    fun getTempUnitFlow(): Flow<Int?>
    fun getSpeedUnitFlow(): Flow<Int?>
    fun isDarkThemeFlow(): Flow<Boolean?>
    fun languageFlow(): Flow<String?>

    suspend fun saveLanguage(language: String)

    suspend fun saveTempUnit(unit: Int)

    suspend fun saveSpeedUnit(unit: Int)

    suspend fun saveIsDarkTheme(isDarkTheme: Boolean)

    suspend fun saveChosenCityId(cityId: Int)

    suspend fun clearPreferences()
}