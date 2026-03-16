package com.msayeh.breeze.presentation.screens.home.ui

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.CityWeatherDetails
import com.msayeh.breeze.domain.model.Coordinates
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Weather
import com.msayeh.breeze.domain.model.WeatherCondition
import com.msayeh.breeze.domain.model.Wind
import com.msayeh.breeze.domain.model.getTodaySlots
import com.msayeh.breeze.domain.model.groupByDay
import com.msayeh.breeze.presentation.common.BottomBarSpacing
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.screens.home.ui.components.currentweather.CurrentWeatherHomeSection
import com.msayeh.breeze.presentation.screens.home.ui.components.currentweather.UpdatingIndicator
import com.msayeh.breeze.presentation.screens.home.ui.components.forecast.DailyForecastSection
import com.msayeh.breeze.presentation.screens.home.ui.components.forecast.TodayForecastSection
import com.msayeh.breeze.presentation.utils.LocationUtils
import com.msayeh.breeze.presentation.screens.home.viewmodel.HomeState
import com.msayeh.breeze.presentation.screens.home.viewmodel.HomeViewModel
import com.msayeh.breeze.presentation.utils.UnitPreferences
import com.msayeh.breeze.presentation.utils.events.UiEventsHandler
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navigateToRoute: (Route) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val unitPreferences by viewModel.unitPreferences.collectAsStateWithLifecycle()
    val selectedCityId by viewModel.selectedCityId.collectAsStateWithLifecycle()
    val app = LocalContext.current.applicationContext
    val lifecycleOwner = rememberLifecycleOwner()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        viewModel.onLocationPermissionResult(granted)
    }

    UiEventsHandler(viewModel.uiEvent, navigateToRoute)

    LaunchedEffect(selectedCityId) {
        if ((selectedCityId == null || selectedCityId == -1) && LocationUtils.checkLocationPermission(app).not()) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            Log.d("SelectedCity", "Resumed")
            viewModel.checkCityId()
        }
    }

    HomeContent(
        uiState,
        unitPreferences,
        viewModel::refreshWeather,
        viewModel::onCityClicked,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeState,
    unitPreferences: UnitPreferences,
    onRefresh: () -> Unit,
    onCityClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        uiState.isLoading,
        onRefresh,
        modifier = modifier,
        state = pullToRefreshState,
        indicator = { },
    ) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            CurrentWeatherHomeSection(
                uiState.cityWeatherDetails?.currentWeather,
                onCityClicked,
                unitPreferences,
                uiState.cityWeatherDetails?.city?.name,
                uiState.isLoading,
            )
            Spacer(Modifier.height(16.dp))
            TodayForecastSection(
                uiState.cityWeatherDetails?.forecastSlots?.getTodaySlots(),
                unitPreferences
            )
            Spacer(Modifier.height(16.dp))
            DailyForecastSection(
                uiState.cityWeatherDetails?.forecastSlots?.groupByDay(),
                unitPreferences
            )
            BottomBarSpacing()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentPreview() {
    HomeContent(
        HomeState(
            cityWeatherDetails = CityWeatherDetails(
                city = City(
                    -1,
                    "Cairo",
                    "EG",
                    Coordinates(15.2, 13.6),
                    isCurrentLocation = true,
                    sortOrder = 5,
                ),
                currentWeather = Weather(
                    cityId = 5,
                    temperature = Temperature(12.0),
                    feelsLike = Temperature(12.0),
                    humidity = 100,
                    wind = Wind(15.0, 23),
                    condition = WeatherCondition("Cloudy", "01n"),
                    pressure = 1000,
                    sunCycle = null,
                    fetchedAt = 1518917,
                ), forecastSlots = emptyList()
            ),
            isLoading = true,
        ),
        UnitPreferences(
            Temperature.Unit.CELSIUS,
            Wind.Unit.METRIC_MS
        ),
        {},
        {},
    )
}