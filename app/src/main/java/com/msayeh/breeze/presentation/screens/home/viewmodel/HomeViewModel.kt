package com.msayeh.breeze.presentation.screens.home.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Wind
import com.msayeh.breeze.domain.repository.PreferencesRepository
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.utils.LocationUtils
import com.msayeh.breeze.presentation.utils.events.UiEvent
import com.msayeh.breeze.presentation.common.dialog.BreezeDialogData
import com.msayeh.breeze.presentation.common.dialog.DialogButton
import com.msayeh.breeze.presentation.common.dialog.DialogButtonType
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.utils.UnitPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val prefRepository: PreferencesRepository,
    private val application: Application,
) : ViewModel() {
    private var selectedCityId: StateFlow<Int?> = prefRepository.getChosenCityIdFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val selectedTempUnit: StateFlow<Temperature.Unit> = prefRepository.getTempUnitFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Temperature.Unit.CELSIUS)

    private val selectedWindSpeedUnit: StateFlow<Wind.Unit> = prefRepository.getSpeedUnitFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Wind.Unit.METRIC_MS)

    val unitPreferences: StateFlow<UnitPreferences> = combine(
        selectedTempUnit,
        selectedWindSpeedUnit
    ) { tempUnit, speedUnit ->
        UnitPreferences(tempUnit, speedUnit)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        UnitPreferences(Temperature.Unit.CELSIUS, Wind.Unit.METRIC_MS)
    )

    private var observationJob: Job? = null

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            selectedCityId.collectLatest { cityId ->
                if (cityId != null) {
                    _uiState.update { it.copy(isCitySelected = true) }
                    observeCityWithWeather()
                    refreshWeather()
                } else {
                    _uiState.update { it.copy(isCitySelected = false) }
                }
            }
        }
    }

    fun refreshWeather() {
        viewModelScope.launch {
            if (selectedCityId.value == null) return@launch
            _uiState.update { it.copy(isLoading = true) }
            val result = weatherRepository.refreshIfStale(selectedCityId.value!!)
            _uiState.update { it.copy(isLoading = false) }

            when (result) {
                is Resource.Error<Unit> -> _uiEvent.emit(
                    UiEvent.ShowSnackbar(
                        application.getString(
                            result.exception.messageResId
                        )
                    )
                )

                is Resource.Success<Unit> -> _uiEvent.emit(
                    UiEvent.ShowSnackbar(
                        application.getString(
                            R.string.weather_refreshed_successfully
                        )
                    )
                )
            }
        }
    }

    fun onCityClicked() {
        viewModelScope.launch {
            _uiEvent.emit(
                UiEvent.NavigateTo(Route.Cities)
            )
        }
    }

    private fun observeCityWithWeather() {
        observationJob?.cancel()
        observationJob = viewModelScope.launch {
            if (selectedCityId.value == null) return@launch
            weatherRepository.observeCityWithWeather(selectedCityId.value!!)
                .collectLatest { cityWeatherDetails ->
                    _uiState.update {
                        it.copy(cityWeatherDetails = cityWeatherDetails)
                    }
                }
        }
    }

    @SuppressLint("MissingPermission")
    fun onLocationPermissionResult(granted: Boolean) {
        if (selectedCityId.value != null) return
        viewModelScope.launch {
            if (granted) {
                val coordinates = LocationUtils.getCurrentLocationCoordinates(application)
                if (coordinates == null) {
                    showPermissionDeniedDialog()
                    return@launch
                }
                val cityResource =
                    weatherRepository.getCityByCoordinates(coordinates, isCurrentLocation = true)
                if (cityResource is Resource.Success && cityResource.data != null) {
                    val addedCityResource = weatherRepository.addCityToFavorites(cityResource.data)
                    addedCityResource.fold(
                        onSuccess = { data ->
                            viewModelScope.launch {
                                prefRepository.saveChosenCityId(data.id)
                            }
                        },
                        onError = { exception ->
                            exception.printStackTrace()
                            showErrorGettingLocationDialog()
                        }
                    )
                } else {
                    (cityResource as Resource.Error<*>).exception.printStackTrace()
                    showErrorGettingLocationDialog()
                }
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    private fun showErrorGettingLocationDialog() {
        viewModelScope.launch {
            _uiEvent.emit(
                UiEvent.ShowDialog(
                    BreezeDialogData.Builder(
                        application.getString(R.string.error_getting_current_location),
                        application.getString(R.string.select_city_manually),
                        positiveButton = DialogButton(
                            application.getString(R.string.choose_manually_instead),
                            onClick = {
                                viewModelScope.launch {
                                    _uiEvent.emit(UiEvent.NavigateTo(Route.Cities))
                                    hideDialog()
                                }
                            }
                        )
                    ).dismissable(false).build()
                )
            )
        }
    }

    private fun showPermissionDeniedDialog() {
        viewModelScope.launch {
            _uiEvent.emit(
                UiEvent.ShowDialog(
                    BreezeDialogData.Builder(
                        application.getString(R.string.location_permission_denied),
                        application.getString(R.string.enable_location_or_choose_city),
                        positiveButton = DialogButton(
                            text = application.getString(R.string.enable_location),
                            onClick = {
                                viewModelScope.launch {
                                    _uiEvent.emit(UiEvent.OpenAppSettings(application.getString(R.string.enable_location_permission)))
                                    while (true) {
                                        delay(1000)
                                        if (LocationUtils.checkLocationPermission(application)) {
                                            hideDialog()
                                        }
                                    }
                                }
                            },
                        ),
                    ).negativeButton(
                        DialogButton(
                            text = application.getString(R.string.choose_city),
                            onClick = {
                                // TODO: Navigate to city selection screen
                                hideDialog()
                            },
                            type = DialogButtonType.ELEVATED
                        )
                    )
                        .dismissable(false).build()
                )
            )
        }
    }
}