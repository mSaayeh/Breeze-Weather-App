package com.msayeh.breeze.presentation.screens.cities.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.repository.PreferencesRepository
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    weatherRepository: WeatherRepository,
    private val prefRepository: PreferencesRepository,
    private val application: Application,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<City>>> =
        MutableStateFlow(UiState.Success(emptyList()))

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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

    fun onAddCityClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateTo(Route.AddCity))
        }
    }
}