package com.msayeh.breeze.presentation.screens.cities.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msayeh.breeze.R
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.screens.cities.ui.components.CityListItem
import com.msayeh.breeze.presentation.screens.cities.viewmodel.CitiesViewModel
import com.msayeh.breeze.presentation.utils.events.UiEventsHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    navigateToRoute: (Route) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCityId by viewModel.selectedCityId.collectAsStateWithLifecycle()

    UiEventsHandler(viewModel.uiEvent, navigateToRoute, navigateUp)

    state.UiHandler(
        onSuccess = { cities ->
            Scaffold(
                floatingActionButton = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SmallFloatingActionButton(
                            onClick = viewModel::onUpdateCurrentLocationClicked,
                            containerColor = MaterialTheme.colorScheme.inversePrimary
                        ) {
                            Icon(
                                Icons.Default.LocationSearching,
                                contentDescription = "Current Location",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        FloatingActionButton(onClick = viewModel::onAddCityClicked) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(stringResource(R.string.manage_locations))
                        },
                        navigationIcon = {
                            IconButton(navigateUp) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                    )
                },
                modifier = modifier,
            ) { padding ->
                if (cities.isEmpty())
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.no_locations_yet),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 22.sp
                            ),
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.use_plus_to_add_locations),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 16.sp
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }
                else
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    val currentLocation = cities.firstOrNull { it.isCurrentLocation }
                    val addedLocations = cities.filterNot { it.isCurrentLocation }

                    if (currentLocation != null)
                        item(currentLocation.id) {
                            Text(
                                stringResource(R.string.current_location),
                                style = MaterialTheme.typography.titleMedium
                            )
                            CityListItem(
                                currentLocation,
                                onCityClicked = {
                                    viewModel.onCityClicked(currentLocation.id)
                                    navigateUp()
                                },
                                isSelected = currentLocation.id == selectedCityId,
                                onDelete = null,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    if (addedLocations.isNotEmpty()) {
                        item {
                            Text(
                                pluralStringResource(
                                    R.plurals.added_locations,
                                    addedLocations.size,
                                    addedLocations.size
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(addedLocations, key = { it.id }) {
                            CityListItem(
                                it,
                                onCityClicked = {
                                    viewModel.onCityClicked(it.id)
                                    navigateUp()
                                },
                                onDelete = {
                                    viewModel.onCityDeleted(it.id)
                                },
                                isSelected = it.id == selectedCityId,
                            )
                            Spacer(modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                }
            }
        },
    )

}