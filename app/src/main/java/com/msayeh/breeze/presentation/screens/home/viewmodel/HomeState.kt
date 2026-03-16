package com.msayeh.breeze.presentation.screens.home.viewmodel

import com.msayeh.breeze.domain.model.CityWeatherDetails

data class HomeState(
    val isLoading: Boolean = false,
    val cityWeatherDetails: CityWeatherDetails? = null,
    val screenErrorMessage: String? = null,
)