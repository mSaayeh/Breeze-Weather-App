package com.msayeh.breeze.presentation.home.viewmodel

import com.msayeh.breeze.domain.model.Weather

data class HomeState(
    val isLoading: Boolean = false,
    val currentWeather: Weather? = null,
    val screenErrorMessage: String? = null,
)