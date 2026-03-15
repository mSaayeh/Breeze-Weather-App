package com.msayeh.breeze.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.data.utils.AppLanguage
import com.msayeh.breeze.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(prefsRepository: PreferencesRepository) :
    ViewModel() {

    val isDarkMode: StateFlow<Boolean?> = prefsRepository.isDarkThemeEnabledFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedLanguage: StateFlow<AppLanguage?> = prefsRepository.getLanguageFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

}