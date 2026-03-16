package com.msayeh.breeze.presentation.screens.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.data.utils.AppLanguage
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Wind
import com.msayeh.breeze.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefsRepository: PreferencesRepository
) : ViewModel() {
    val isDarkModeEnabled = prefsRepository.isDarkThemeEnabledFlow()
        .localStateIn(false)

    val language: StateFlow<AppLanguage> = prefsRepository.getLanguageFlow()
        .localStateIn(AppLanguage.fromCode(Locale.getDefault().language))

    val tempUnit = prefsRepository.getTempUnitFlow()
        .localStateIn(Temperature.Unit.CELSIUS)

    val speedUnit = prefsRepository.getSpeedUnitFlow()
        .localStateIn(Wind.Unit.METRIC_MS)

    fun setDarkMode(isEnabled: Boolean) {
        if (isEnabled == this.isDarkModeEnabled.value) return
        viewModelScope.launch {
            prefsRepository.saveDarkTheme(isEnabled)
        }
    }

    fun setLanguage(language: AppLanguage) {
        if (language == this.language.value) return
        viewModelScope.launch {
            prefsRepository.saveLanguage(language)
        }
    }

    fun setTempUnit(unit: Temperature.Unit) {
        if (unit == this.tempUnit.value) return
        viewModelScope.launch {
            prefsRepository.saveTempUnit(unit)
        }
    }

    fun setSpeedUnit(unit: Wind.Unit) {
        if (unit == this.speedUnit.value) return
        viewModelScope.launch {
            prefsRepository.saveSpeedUnit(unit)
        }
    }

    private fun <T> Flow<T>.localStateIn(initialValue: T): StateFlow<T> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initialValue)
}