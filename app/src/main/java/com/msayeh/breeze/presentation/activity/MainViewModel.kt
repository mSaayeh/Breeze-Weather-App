package com.msayeh.breeze.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.data.utils.AppLanguage
import com.msayeh.breeze.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val prefsRepository: PreferencesRepository) :
    ViewModel() {

    val isDarkMode: StateFlow<Boolean?> = prefsRepository.isDarkThemeEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedLanguage: StateFlow<AppLanguage?> = prefsRepository.getLanguage()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

}