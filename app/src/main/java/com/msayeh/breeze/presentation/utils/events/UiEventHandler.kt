package com.msayeh.breeze.presentation.utils.events

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.msayeh.breeze.presentation.activity.LocalDialogState
import com.msayeh.breeze.presentation.activity.LocalSnackbarHost
import com.msayeh.breeze.presentation.navigation.Route
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UiEventsHandler(uiEventSharedFlow: SharedFlow<UiEvent>, handleNavigation: ((Route) -> Unit)? = null, navigateBack: (() -> Unit)? = null) {
    val snackbarHost = LocalSnackbarHost.current
    val dialogState = LocalDialogState.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiEventSharedFlow) {
        uiEventSharedFlow.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> handleNavigation?.invoke(event.route) ?: throw IllegalArgumentException("No navigation handler provided")
                is UiEvent.ShowSnackbar -> snackbarHost.showSnackbar(event.message)
                is UiEvent.ShowDialog -> {
                    Log.d("DialogContainer", "ShowDialog: ${event.dialog}")
                    dialogState.showDialog(event.dialog)
                }
                is UiEvent.HideDialog -> dialogState.hideDialog()
                is UiEvent.OpenAppSettings -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    Toast.makeText(context, event.toastMessage, Toast.LENGTH_SHORT).show()
                    context.startActivity(intent)
                }
                is UiEvent.NavigateBack -> {
                    navigateBack?.invoke() ?: throw IllegalArgumentException("No Navigate back handler provided")
                }
                is UiEvent.RemoveFocus -> {
                    focusManager.clearFocus()
                }
            }
        }
    }
}