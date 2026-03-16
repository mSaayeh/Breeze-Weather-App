package com.msayeh.breeze.presentation.screens.addcity.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.Coordinates
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.utils.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class AddCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val application: Application
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCityState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var searchJob: Job? = null

    fun onSaveCityClicked() {
        viewModelScope.launch {
            val cityResource = uiState.value.markerPosition?.toCoordinates()?.let {
                weatherRepository.getCityByCoordinates(it)
            } ?: return@launch
            when (cityResource) {
                is Resource.Error<*> -> {
                    _uiEvent.emit(UiEvent.ShowSnackbar(application.getString(cityResource.exception.messageResId)))
                }

                is Resource.Success<City> -> {
                    when (val cityAdditionResource =
                        weatherRepository.addCityToFavorites(cityResource.data)) {
                        is Resource.Error<*> -> {
                            _uiEvent.emit(
                                UiEvent.ShowSnackbar(
                                    application.getString(
                                        cityAdditionResource.exception.messageResId
                                    )
                                )
                            )
                        }

                        is Resource.Success<*> -> {
                            _uiEvent.emit(UiEvent.ShowSnackbar(application.getString(R.string.location_added_successfully)))
                            _uiEvent.emit(UiEvent.NavigateBack)
                        }
                    }
                }
            }
        }
    }

    fun addMarker(latLng: LatLng) {
        _uiState.update { it.copy(markerPosition = latLng) }
    }

    fun onAutoCompleteCityClicked(city: City) {
        _uiState.update {
            it.copy(
                markerPosition = city.coordinates.toLatLng(),
                cameraPosition = city.coordinates.toLatLng(),
                autoCompleteCities = emptyList(),
            )
        }
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.RemoveFocus)
        }
    }

    fun onSearchValueChanged(query: String) {
        searchJob?.cancel()
        searchJob = null

        _uiState.update {
            it.copy(
                searchQuery = query,
                autoCompleteCities = if (query.isBlank()) emptyList() else it.autoCompleteCities
            )
        }

        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            delay(500.milliseconds)
            when (val citiesResource = weatherRepository.getCitySuggestions(query)) {
                is Resource.Error -> {
                    _uiEvent.emit(
                        UiEvent.ShowSnackbar(
                            application.getString(citiesResource.exception.messageResId)
                        )
                    )
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(autoCompleteCities = citiesResource.data) }
                }
            }
        }
    }
}