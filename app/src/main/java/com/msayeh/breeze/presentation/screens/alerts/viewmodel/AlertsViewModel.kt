package com.msayeh.breeze.presentation.screens.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.domain.model.AlertCityDetails
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.navigation.Route
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
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
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

    fun onAddAlertClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateTo(Route.AddAlert))
        }
    }

    fun deleteAlert(alertId: Int) {
        viewModelScope.launch {
            weatherRepository.deleteAlert(alertId)
        }
    }
}
