package com.msayeh.breeze.presentation.screens.cities.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.PreferencesRepository
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.common.dialog.BreezeDialogData
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.utils.LocationUtils
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

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val prefRepository: PreferencesRepository,
    private val application: Application,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<City>>> =
        MutableStateFlow(UiState.Success(emptyList()))

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var permissionCheckJob: Job? = null
    private var updateCurrentLocationJob: Job? = null

    private val cities = weatherRepository.observeAllCities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedCityId = prefRepository.getChosenCityIdFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val uiState = combine(_uiState, cities) { state, cities ->
        if (state is UiState.Success) {
            state.copy(data = cities)
        } else {
            state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Success(emptyList()))

    fun onCityClicked(clickedCityId: Int) {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            prefRepository.saveChosenCityId(clickedCityId)
            _uiState.update { UiState.Success(emptyList()) }
            _uiEvent.emit(UiEvent.NavigateBack)
        }
    }

    fun onCityDeleted(deletedCityId: Int) {
        viewModelScope.launch {
            weatherRepository.removeCityFromFavorites(deletedCityId)
            if (selectedCityId.value == deletedCityId) {
                prefRepository.saveChosenCityId(-1)
            }
            _uiEvent.emit(
                UiEvent.ShowSnackbar(
                    application.getString(R.string.city_deleted_successfully)
                )
            )
        }
    }

    fun onAddCityClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateTo(Route.AddCity))
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun updateCurrentLocation() {
        val coordinates = LocationUtils.getCurrentLocationCoordinates(application)
        if (coordinates == null) {
            _uiEvent.emit(UiEvent.ShowSnackbar(application.getString(R.string.error_getting_current_location)))
            return
        }
        when (val cityResource =
            weatherRepository.getCityByCoordinates(coordinates, isCurrentLocation = true)) {
            is Resource.Error<City> -> _uiEvent.emit(
                UiEvent.ShowSnackbar(
                    application.getString(
                        cityResource.exception.messageResId
                    )
                )
            )

            is Resource.Success<City> -> when (val cityAdditionResource =
                weatherRepository.addCityToFavorites(cityResource.data)) {
                is Resource.Error<*> -> _uiEvent.emit(
                    UiEvent.ShowSnackbar(
                        application.getString(
                            cityAdditionResource.exception.messageResId
                        )
                    )
                )

                is Resource.Success<City> -> _uiEvent.emit(
                    UiEvent.ShowSnackbar(
                        application.getString(
                            R.string.current_location_updated
                        )
                    )
                )
            }
        }

    }

    fun onUpdateCurrentLocationClicked() {
        updateCurrentLocationJob?.cancel()
        updateCurrentLocationJob = viewModelScope.launch {
            if (!LocationUtils.checkLocationPermission(application)) {
                _uiEvent.emit(UiEvent.OpenAppSettings(application.getString(R.string.enable_location_permission)))
                permissionCheckJob?.cancel()
                permissionCheckJob = viewModelScope.launch {
                    while (true) {
                        delay(500L)
                        if (LocationUtils.checkLocationPermission(application)) {
                            updateCurrentLocation()
                            break
                        }
                    }
                }
            } else {
                if (LocationUtils.isLocationEnabled(application)) {
                    updateCurrentLocation()
                } else {
                    _uiEvent.emit(UiEvent.ShowSnackbar(application.getString(R.string.please_enable_location_services)))
                }
            }
        }
    }
}