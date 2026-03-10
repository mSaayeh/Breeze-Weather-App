package com.msayeh.breeze.presentation.common

import com.msayeh.breeze.presentation.Route

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class NavigateTo(val route: Route) : UiEvent()
}