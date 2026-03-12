package com.msayeh.breeze.presentation.home.viewmodel

import com.msayeh.breeze.domain.model.CityWeatherDetails

data class HomeState(
    val isLoading: Boolean = false,
    val cityWeatherDetails: CityWeatherDetails? = null,
    val isCitySelected: Boolean = false,
    val screenErrorMessage: String? = null,
)