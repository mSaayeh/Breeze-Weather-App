package com.msayeh.breeze.presentation.screens.addalerts.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.AlertType
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.utils.AlarmPermissionManager
import com.msayeh.breeze.presentation.utils.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddAlertViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val application: Application,
    private val alarmPermissionManager: AlarmPermissionManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddAlertState())
    private val cities = weatherRepository.observeAllCities()

    val uiState = combine(_uiState, cities) { state, cities ->
        val selectedCityId = state.selectedCityId?.takeIf { id -> cities.any { it.id == id } }
        state.copy(
            cities = cities,
            selectedCityId = selectedCityId,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddAlertState())

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onCitySelected(cityId: Int) {
        _uiState.update { it.copy(selectedCityId = cityId) }
    }

    fun onTimeSelected(time: LocalTime) {
        _uiState.update { it.copy(time = time) }
    }

    fun onTypeSelected(type: AlertType) {
        if (type == AlertType.ALARM && !alarmPermissionManager.canScheduleExactAlarms()) {
            viewModelScope.launch {
                _uiEvent.emit(
                    UiEvent.NavigateWithIntent(
                        Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            data = Uri.fromParts("package", application.packageName, null)
                        }
                    )
                )
            }
            return
        }
        _uiState.update { it.copy(type = type) }
    }

    fun onManageLocationsClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateTo(Route.Cities))
        }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            val current = uiState.value
            val cityId = current.selectedCityId
            if (cityId == null) {
                _uiEvent.emit(UiEvent.ShowSnackbar(application.getString(R.string.please_select_city)))
                return@launch
            }

            _uiState.update { it.copy(isSaving = true) }
            when (val result = weatherRepository.upsertAlert(
                Alert(
                    cityId = cityId,
                    isEnabled = true,
                    time = current.time,
                    type = current.type,
                )
            )) {
                is Resource.Error -> {
                    _uiEvent.emit(
                        UiEvent.ShowSnackbar(
                            application.getString(result.exception.messageResId)
                        )
                    )
                }

                is Resource.Success -> {
                    _uiEvent.emit(UiEvent.NavigateBack)
                    _uiEvent.emit(
                        UiEvent.ShowSnackbar(
                            application.getString(R.string.alert_added_successfully)
                        )
                    )
                }
            }
            _uiState.update { it.copy(isSaving = false) }
        }
    }
}

