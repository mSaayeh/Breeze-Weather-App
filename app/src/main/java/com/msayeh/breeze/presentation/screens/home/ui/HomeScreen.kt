package com.msayeh.breeze.presentation.screens.home.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msayeh.breeze.presentation.utils.LocationUtils
import com.msayeh.breeze.presentation.utils.UiEventsHandler
import com.msayeh.breeze.presentation.screens.home.viewmodel.HomeState
import com.msayeh.breeze.presentation.screens.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val app = LocalContext.current.applicationContext

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        viewModel.onLocationPermissionResult(granted)
    }

    UiEventsHandler(viewModel.uiEvent)

    LaunchedEffect(uiState.isCitySelected) {
        if (uiState.isCitySelected.not() && LocationUtils.checkLocationPermission(app).not()) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    HomeContent(
        uiState,
        viewModel::refreshWeather,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
fun HomeContent(uiState: HomeState, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Home Screen")
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.cityWeatherDetails != null) {
            Text(uiState.cityWeatherDetails.currentWeather?.temperature.toString())
            Text("Feels Like: ${uiState.cityWeatherDetails.currentWeather?.feelsLike}")
        }
        Button(onRefresh) {
            Text("Refresh")
        }
    }
}