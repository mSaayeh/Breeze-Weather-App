package com.msayeh.breeze.presentation.screens.addcity.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.msayeh.breeze.R
import com.msayeh.breeze.presentation.screens.addcity.viewmodel.AddCityViewModel
import com.msayeh.breeze.presentation.utils.events.UiEventsHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCityScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddCityViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UiEventsHandler(viewModel.uiEvent, navigateBack = navigateUp)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uiState.cameraPosition, 14f)
    }

    LaunchedEffect(uiState.cameraPosition) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(uiState.cameraPosition, 14f)
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_location)) },
                navigationIcon = {
                    IconButton(navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLongClick = { latLng ->
                    viewModel.addMarker(latLng)
                },
            ) {
                uiState.markerPosition?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Location",
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchValueChanged,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text(stringResource(R.string.search_location)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                )
                Spacer(Modifier.height(4.dp))
                AnimatedVisibility(
                    uiState.autoCompleteCities.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .heightIn(min = 0.dp, max = 260.dp),
                    ) {
                        LazyColumn {
                            items(uiState.autoCompleteCities) {
                                ListItem(
                                    headlineContent = {
                                        Text(it.name, style = MaterialTheme.typography.bodyLarge)
                                    }, supportingContent = {
                                        Text(
                                            it.country, style = MaterialTheme.typography.bodyMedium
                                        )
                                    }, modifier = Modifier.clickable(
                                        onClick = {
                                            viewModel.onAutoCompleteCityClicked(
                                                it
                                            )
                                        },
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                onClick = viewModel::onSaveCityClicked,
                enabled = uiState.isSaveEnabled,
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                )
                Spacer(Modifier.width(16.dp))
                Text(stringResource(R.string.save_location))
            }
        }
    }
}
