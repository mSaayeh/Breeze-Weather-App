package com.msayeh.breeze.presentation.home.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.domain.exception.LocalizedException
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.model.Weather
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.home.ui.HomeState
import com.msayeh.breeze.presentation.home.ui.HomeUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val application: Application,
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        updateCurrentWeather()
    }

    fun updateCurrentWeather() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = weatherRepository.getCurrentWeather(40.7128, -74.0060)) {
                is Resource.Error<*> -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(HomeUiEvent.ShowSnackbar(application.getString(result.exception.messageResId)))
                }
                is Resource.Loading<*> -> _uiState.update { it.copy(isLoading = true) }
                is Resource.Success<Weather> -> _uiState.update { it.copy(isLoading = false, currentWeather = result.data) }
            }
        }
    }
}