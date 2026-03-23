package com.msayeh.breeze.presentation.screens.alerts.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.exception.MissingPermissionException
import com.msayeh.breeze.presentation.common.navbar.BottomBarSpacing
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.screens.alerts.ui.components.AlertItem
import com.msayeh.breeze.presentation.screens.alerts.viewmodel.AlertsViewModel
import com.msayeh.breeze.presentation.utils.UiState
import com.msayeh.breeze.presentation.utils.events.UiEventsHandler

@Composable
fun AlertsScreen(
    navigateToRoute: (Route) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) @RequiresApi(Build.VERSION_CODES.TIRAMISU) { permissions ->
        if (activity == null) return@rememberLauncherForActivityResult
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (!activity.shouldShowRequestPermissionRationale(permission) && permissions[permission] == false) {
            viewModel.requestOpenAppSettings()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
    }

    UiEventsHandler(viewModel.uiEvent, navigateToRoute)

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            Text(
                text = stringResource(R.string.weather_alerts),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(top = 16.dp, start = 16.dp)
            )
        },
        floatingActionButton = {
            if (uiState is UiState.Success) {
                FloatingActionButton(
                    onClick = viewModel::onAddAlertClicked,
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(16.dp)
                        .padding(bottom = 64.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { padding ->
        uiState.UiHandler(
            onError = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(
                        stringResource(it.messageResId),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (it is MissingPermissionException) {
                        Button(
                            onClick = {
                                viewModel.onGrantPermissionClicked()
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.POST_NOTIFICATIONS
                                        )
                                    )
                                }
                            }
                        ) {
                            Text(stringResource(R.string.grant_permission))
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { alerts ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                if (alerts.isEmpty())
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.no_alerts_yet),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 22.sp
                            ),
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.use_plus_to_add_alerts),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 16.sp
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }
                else
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                    ) {
                        items(alerts, key = { it.alert.id }) {
                            AlertItem(
                                it,
                                onDelete = { viewModel.deleteAlert(it.alert.id) },
                                onEnabledSwitchChange = { isEnabled ->
                                    viewModel.onAlertEnabledChanged(it.alert.id, isEnabled)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                BottomBarSpacing()
            }
        }
    }
}
