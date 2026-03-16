package com.msayeh.breeze.data.preferences.repository

import com.msayeh.breeze.data.preferences.datasource.PreferencesDataSource
import com.msayeh.breeze.data.utils.AppLanguage
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Wind
import com.msayeh.breeze.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import java.util.Locale
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

    override fun isDarkThemeEnabledFlow(): Flow<Boolean?> = preferencesDataSource.isDarkThemeFlow()

    override suspend fun saveDarkTheme(isDarkThemeEnabled: Boolean) =
        preferencesDataSource.saveIsDarkTheme(isDarkThemeEnabled)

    override fun getLanguageFlow(): Flow<AppLanguage> = preferencesDataSource.languageFlow().map {
        AppLanguage.fromCode(it ?: Locale.getDefault().language)
    }

    override suspend fun getLanguage(): AppLanguage {
        val languageCode = preferencesDataSource.languageFlow().firstOrNull()
        val language = AppLanguage.fromCode(languageCode ?: Locale.getDefault().language)
        return language
    }

    override suspend fun saveLanguage(appLanguage: AppLanguage) =
        preferencesDataSource.saveLanguage(appLanguage.code)

    override fun getTempUnitFlow(): Flow<Temperature.Unit> =
        preferencesDataSource.getTempUnitFlow().map {
            if (it == null) return@map Temperature.Unit.CELSIUS
            Temperature.Unit.fromCode(it)
        }

    override suspend fun saveTempUnit(unit: Temperature.Unit) =
        preferencesDataSource.saveTempUnit(unit.code)

    override fun getSpeedUnitFlow(): Flow<Wind.Unit> = preferencesDataSource.getSpeedUnitFlow().map {
        if (it == null) return@map Wind.Unit.METRIC_MS
        Wind.Unit.fromCode(it)
    }

    override suspend fun saveSpeedUnit(unit: Wind.Unit) =
        preferencesDataSource.saveSpeedUnit(unit.code)

    override suspend fun clearPreferences() = preferencesDataSource.clearPreferences()
}