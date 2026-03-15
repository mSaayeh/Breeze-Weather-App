package com.msayeh.breeze.presentation.screens.cities.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.screens.cities.ui.components.CityListItem
import com.msayeh.breeze.presentation.screens.cities.viewmodel.CitiesViewModel
import com.msayeh.breeze.presentation.utils.UiState
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

    UiEventsHandler(viewModel.uiEvent, navigateToRoute, navigateUp)

    state.UiHandler(
        onSuccess = {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = viewModel::onAddCityClicked) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                },
                topBar = {
                    TopAppBar(
                        {
                            Text(stringResource(R.string.manage_cities))
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
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items((state as UiState.Success).data) {
                        CityListItem(
                            it,
                            onCityClicked = {
                                viewModel.onCityClicked(it.id)
                                navigateUp()
                            },
                        )
                    }
                }
            }
        },
    )

}