package com.msayeh.breeze.data.preferences.repository

import com.msayeh.breeze.data.preferences.datasource.PreferencesDataSource
import com.msayeh.breeze.data.utils.AppLanguage
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Wind
import com.msayeh.breeze.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
) : PreferencesRepository {
    override suspend fun checkAndMarkFirstLaunch(): Boolean =
        preferencesDataSource.isFirstTimeUser().also {
            if (it) {
                preferencesDataSource.saveFirstTimeUser(false)
            }
        }

    override fun getChosenCityIdFlow(): Flow<Int?> = preferencesDataSource.chosenCityIdFlow()

    override suspend fun saveChosenCityId(cityId: Int) =
        preferencesDataSource.saveChosenCityId(cityId)

    override fun isDarkThemeEnabled(): Flow<Boolean?> = preferencesDataSource.isDarkThemeFlow()

    override suspend fun saveDarkTheme(isDarkThemeEnabled: Boolean) =
        preferencesDataSource.saveIsDarkTheme(isDarkThemeEnabled)

    override fun getLanguage(): Flow<AppLanguage?> = preferencesDataSource.languageFlow().map {
        if (it == null) return@map null
        AppLanguage.fromCode(it)
    }

    override suspend fun saveLanguage(appLanguage: AppLanguage) =
        preferencesDataSource.saveLanguage(appLanguage.code)

    override fun getTempUnit(): Flow<Temperature.Unit?> =
        preferencesDataSource.getTempUnitFlow().map {
            if (it == null) return@map null
            Temperature.Unit.fromCode(it)
        }

    override suspend fun saveTempUnit(unit: Temperature.Unit) =
        preferencesDataSource.saveTempUnit(unit.code)

    override fun getSpeedUnit(): Flow<Wind.Unit?> = preferencesDataSource.getSpeedUnitFlow().map {
        if (it == null) return@map null
        Wind.Unit.fromCode(it)
    }

    override suspend fun saveSpeedUnit(unit: Wind.Unit) =
        preferencesDataSource.saveSpeedUnit(unit.code)

    override suspend fun clearPreferences() = preferencesDataSource.clearPreferences()
}