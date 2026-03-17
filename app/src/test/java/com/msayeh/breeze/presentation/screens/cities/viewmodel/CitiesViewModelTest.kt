package com.msayeh.breeze.presentation.screens.cities.viewmodel

import android.app.Application
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.PreferencesRepository
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.utils.UiState
import com.msayeh.breeze.presentation.utils.events.UiEvent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class CitiesViewModelTest {

    private val weatherRepository: WeatherRepository = mockk()
    private val prefRepository: PreferencesRepository = mockk()
    private val application: Application = mockk()

    private lateinit var viewModel: CitiesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        every { weatherRepository.observeAllCities() } returns flowOf(emptyList())
        every { prefRepository.getChosenCityIdFlow() } returns flowOf(null)

        viewModel = CitiesViewModel(weatherRepository, prefRepository, application)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    // onAddCityClicked
    @Test
    fun onAddCityClicked_emitsNavigateToAddCity() = runTest {
        // Given
        val events = mutableListOf<UiEvent>()
        val job = launch { viewModel.uiEvent.toList(events) }

        // When
        viewModel.onAddCityClicked()
        advanceUntilIdle()

        // Then
        assertThat(events, hasItem(UiEvent.NavigateTo(Route.AddCity)))
        job.cancel()
    }

    //onCityClicked
    @Test
    fun onCityClicked_savesChosenCityIdAndEmitsNavigateBack() = runTest {
        // Given
        coEvery { prefRepository.saveChosenCityId(1) } just Runs
        val events = mutableListOf<UiEvent>()
        val job = launch { viewModel.uiEvent.toList(events) }

        // When
        viewModel.onCityClicked(1)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { prefRepository.saveChosenCityId(1) }
        assertThat(events, hasItem(UiEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun onCityClicked_stateIsSuccessAfterCompletion() = runTest {
        // Given
        coEvery { prefRepository.saveChosenCityId(1) } just Runs

        // When
        viewModel.onCityClicked(1)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value, instanceOf(UiState.Success::class.java))
    }

    //onCityDeleted
    @Test
    fun onCityDeleted_callsRemoveCityAndEmitsSnackbar() = runTest {
        // Given
        coEvery { weatherRepository.removeCityFromFavorites(1) } returns Resource.Success(Unit)
        every { application.getString(any()) } returns "City deleted"
        val events = mutableListOf<UiEvent>()
        val job = launch { viewModel.uiEvent.toList(events) }

        // When
        viewModel.onCityDeleted(1)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { weatherRepository.removeCityFromFavorites(1) }
        assertThat(events.any { it is UiEvent.ShowSnackbar }, `is`(true))
        job.cancel()
    }

    @Test
    fun onCityDeleted_callsRemoveCityFromFavorites() = runTest {
        // Given
        coEvery { weatherRepository.removeCityFromFavorites(1) } returns Resource.Success(Unit)
        every { application.getString(any()) } returns "City deleted"

        // When
        viewModel.onCityDeleted(1)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { weatherRepository.removeCityFromFavorites(1) }
    }
}