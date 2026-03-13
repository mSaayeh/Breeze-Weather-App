package com.msayeh.breeze.presentation.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.msayeh.breeze.presentation.activity.LocalDialogState
import com.msayeh.breeze.presentation.activity.LocalSnackbarHost
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.common.dialog.BreezeDialogData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class NavigateTo(val route: Route) : UiEvent()
    data class ShowDialog(val dialog: BreezeDialogData) : UiEvent()
    data class OpenAppSettings(val toastMessage: String) : UiEvent()
}

@Composable
fun UiEventsHandler(uiEventStateFlow: SharedFlow<UiEvent>) {
    val snackbarHost = LocalSnackbarHost.current
    val dialogState = LocalDialogState.current
    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        uiEventStateFlow.collectLatest { event ->
            when (event) {
                is UiEvent.NavigateTo -> navController.navigate(event.route)
                is UiEvent.ShowSnackbar -> snackbarHost.showSnackbar(event.message)
                is UiEvent.ShowDialog -> dialogState.showDialog(event.dialog)
                is UiEvent.OpenAppSettings -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    Toast.makeText(context, event.toastMessage, Toast.LENGTH_SHORT).show()
                    context.startActivity(intent)
                }
            }
        }
    }
}