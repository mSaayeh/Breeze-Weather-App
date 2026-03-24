package com.msayeh.breeze.presentation.screens.alerts.viewmodel

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.exception.MissingPermissionException
import com.msayeh.breeze.domain.model.AlertCityDetails
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.utils.UiState
import com.msayeh.breeze.presentation.utils.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val application: Application
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<AlertCityDetails>>> =
        MutableStateFlow(UiState.Success(emptyList()))
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

    private var isGrantPermissionClicked = false

    private var checkPermissionJob: Job? = null

    init {
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                application,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                _uiState.update {
                    UiState.Error(
                        MissingPermissionException(
                            R.string.missing_location_permission
                        )
                    )
                }
            } else {
                checkPermissionJob?.cancel()
                checkPermissionJob = null
                if (_uiState.value is UiState.Error) {
                    _uiState.value = UiState.Success(emptyList())
                }
            }
        }
    }

    private fun checkNotificationPermissionPeriodically() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            checkPermissionJob?.cancel()
            checkPermissionJob = null
            checkPermissionJob = viewModelScope.launch {
                while (true) {
                    checkNotificationPermission()
                    delay(1.seconds)
                }
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        if (granted) {
            checkNotificationPermission()
        } else {
            _uiState.update {
                UiState.Error(
                    MissingPermissionException(
                        R.string.missing_location_permission
                    )
                )
            }
        }
    }

    fun requestOpenAppSettings() {
        if (!isGrantPermissionClicked) return
        isGrantPermissionClicked = false
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.OpenAppSettings(application.getString(R.string.enable_notification_permission)))
        }
    }

    fun onGrantPermissionClicked() {
        isGrantPermissionClicked = true
        checkNotificationPermissionPeriodically()
    }

    fun onAlertEnabledChanged(alertId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            weatherRepository.updateAlertEnabled(alertId, isEnabled)
        }
    }

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
