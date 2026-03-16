package com.msayeh.breeze.presentation.screens.alerts.ui

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msayeh.breeze.presentation.common.navbar.BottomBarSpacing
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.screens.alerts.ui.components.AlertItem
import com.msayeh.breeze.presentation.screens.alerts.viewmodel.AlertsViewModel
import com.msayeh.breeze.presentation.utils.events.UiEventsHandler
import java.time.format.DateTimeFormatter

@Composable
fun AlertsScreen(
    navigateToRoute: (Route) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UiEventsHandler(viewModel.uiEvent, navigateToRoute)

    uiState.UiHandler { alerts ->
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = viewModel::addSampleAlert,
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(bottom = 64.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                Text(text = "Weather Alerts", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(alerts, key = { it.alert.id }) {
                        AlertItem(
                            it,
                            onDelete = { viewModel.deleteAlert(it.alert.id) },
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