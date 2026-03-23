package com.msayeh.breeze.presentation.screens.addalerts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.AlertType
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.screens.addalerts.viewmodel.AddAlertViewModel
import com.msayeh.breeze.presentation.common.SettingsSelector
import com.msayeh.breeze.presentation.utils.events.UiEventsHandler
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertScreen(
    navigateUp: () -> Unit,
    navigateToRoute: (Route) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddAlertViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    UiEventsHandler(viewModel.uiEvent, handleNavigation = navigateToRoute, navigateBack = navigateUp)

    var isCityMenuExpanded by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val selectedCity = state.cities.firstOrNull { it.id == state.selectedCityId }

    if (showTimePicker) {
        AlertTimePickerDialog(
            initialTime = state.time,
            onDismiss = { showTimePicker = false },
            onConfirm = { viewModel.onTimeSelected(it) },
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_alert)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            if (state.cities.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_locations_added),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = viewModel::onManageLocationsClicked) {
                    Text(stringResource(R.string.manage_locations))
                }
                return@Column
            }

            ExposedDropdownMenuBox(
                expanded = isCityMenuExpanded,
                onExpandedChange = { isCityMenuExpanded = !isCityMenuExpanded },
            ) {
                OutlinedTextField(
                    value = selectedCity?.displayName() ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.city)) },
                    placeholder = { Text(stringResource(R.string.select_city)) },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCityMenuExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                )

                ExposedDropdownMenu(
                    expanded = isCityMenuExpanded,
                    onDismissRequest = { isCityMenuExpanded = false },
                ) {
                    state.cities.forEach { city ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = city.displayName(),
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            },
                            onClick = {
                                viewModel.onCitySelected(city.id)
                                isCityMenuExpanded = false
                            },
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.alert_type),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(8.dp))
            SettingsSelector(
                options = listOf(AlertType.NOTIFICATION, AlertType.ALARM),
                optionNames = listOf(R.string.notification, R.string.alarm),
                selectedItem = state.type,
                onItemSelected = viewModel::onTypeSelected,
            )

            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.alert_time)) },
                    supportingContent = {
                        Text(
                            state.time.format(DateTimeFormatter.ofPattern("h:mm a")),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.Default.AccessTime, contentDescription = null)
                    },
                    modifier = Modifier.clickable { showTimePicker = true },
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = viewModel::onSaveClicked,
                enabled = state.isSaveEnabled,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.save_alert))
            }
        }
    }
}

@Composable
private fun City.displayName(): String {
    return if (isCurrentLocation) stringResource(R.string.current_location) else name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlertTimePickerDialog(
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit,
) {
    val pickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = false,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(LocalTime.of(pickerState.hour, pickerState.minute))
                    onDismiss()
                }
            ) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        text = {
            TimePicker(state = pickerState)
        },
    )
}
