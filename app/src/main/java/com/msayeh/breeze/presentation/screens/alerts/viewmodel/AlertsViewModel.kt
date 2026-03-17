package com.msayeh.breeze.presentation.screens.alerts.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.AlertCityDetails
import com.msayeh.breeze.domain.model.AlertType
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.utils.UiState
import com.msayeh.breeze.presentation.utils.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val application: Application,
): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<AlertCityDetails>>> = MutableStateFlow(UiState.Success(emptyList()))
    private val alerts = weatherRepository.observeAllAlerts()
    val uiState = combine(_uiState, alerts) { state, alerts ->
        if (state is UiState.Success) {
            state.copy(data = alerts)
        } else {
            state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Success(emptyList()))


    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun addSampleAlert() {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            weatherRepository.upsertAlert(
                Alert(
                    cityId = -1,
                    isEnabled = true,
                    time = LocalTime.now().plusSeconds(10),
                    type = AlertType.ALARM,
                )
            )
            _uiState.emit(UiState.Success(emptyList()))
        }
    }

    fun deleteAlert(alertId: Int) {
        viewModelScope.launch {
            weatherRepository.deleteAlert(alertId)
        }
    }
}