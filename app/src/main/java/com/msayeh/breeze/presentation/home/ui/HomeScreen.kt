package com.msayeh.breeze.presentation.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.msayeh.breeze.LocalSnackbarHost
import com.msayeh.breeze.presentation.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHost.current
    val navController = rememberNavController()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when(event) {
                is HomeUiEvent.NavigateTo -> navController.navigate(event.route)
                is HomeUiEvent.ShowSnackbar -> snackbarHost.showSnackbar(event.message)
            }
        }
    }
    HomeContent(
        uiState,
        viewModel::updateCurrentWeather,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
fun HomeContent(uiState: HomeState, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    Column {
        Text("Home Screen")
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.currentWeather != null) {
            Text(uiState.currentWeather.cityName)
            Text(uiState.currentWeather.temperature.toString())
            Text("Feels Like: ${uiState.currentWeather.feelsLike}")
        }
    }
}