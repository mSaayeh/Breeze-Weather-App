package com.msayeh.breeze.presentation.home.ui

import com.msayeh.breeze.presentation.Route

sealed class HomeUiEvent {
    data class ShowSnackbar(val message: String) : HomeUiEvent()
    data class NavigateTo(val route: Route) : HomeUiEvent()
}