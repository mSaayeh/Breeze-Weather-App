package com.msayeh.breeze.presentation.utils.events

import com.msayeh.breeze.presentation.common.dialog.BreezeDialogData
import com.msayeh.breeze.presentation.navigation.Route

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class NavigateTo(val route: Route) : UiEvent()
    data class ShowDialog(val dialog: BreezeDialogData) : UiEvent()
    object HideDialog : UiEvent()
    data class OpenAppSettings(val toastMessage: String) : UiEvent()
    object NavigateBack : UiEvent()
    object RemoveFocus : UiEvent()
}