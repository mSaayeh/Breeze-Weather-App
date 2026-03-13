package com.msayeh.breeze.data.preferences.datasource

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(private val context: Application) : PreferencesDataSource {
    private val CHOSEN_CITY_ID = intPreferencesKey("chosen_city_id")
    private val LANGUAGE = stringPreferencesKey("language")
    private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    private val FIRST_TIME_USER = booleanPreferencesKey("first_time_user")
    private val TEMP_UNIT = intPreferencesKey("temp_unit")
    private val SPEED_UNIT = intPreferencesKey("speed_unit")

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    override fun chosenCityIdFlow(): Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[CHOSEN_CITY_ID]
    }

    override suspend fun isFirstTimeUser(): Boolean =
        context.dataStore.data.map { preferences ->
            preferences[FIRST_TIME_USER] ?: true
        }.first()

    override suspend fun saveFirstTimeUser(isFirstTimeUser: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_TIME_USER] = isFirstTimeUser
        }
    }

    override fun getTempUnitFlow(): Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[TEMP_UNIT]
    }

    override fun getSpeedUnitFlow(): Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[SPEED_UNIT]
    }

    override fun isDarkThemeFlow(): Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME]
    }

    override fun languageFlow(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE]
    }

    override suspend fun saveLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }

    override suspend fun saveTempUnit(unit: Int) {
        context.dataStore.edit { preferences ->
            preferences[TEMP_UNIT] = unit
        }
    }

    override suspend fun saveSpeedUnit(unit: Int) {
        context.dataStore.edit { preferences ->
            preferences[SPEED_UNIT] = unit
        }
    }

    override suspend fun saveIsDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDarkTheme
        }
    }

    override suspend fun saveChosenCityId(cityId: Int) {
        context.dataStore.edit { preferences ->
            preferences[CHOSEN_CITY_ID] = cityId
        }
    }

    override suspend fun clearPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}