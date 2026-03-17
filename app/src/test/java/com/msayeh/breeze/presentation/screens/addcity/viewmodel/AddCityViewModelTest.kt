package com.msayeh.breeze.presentation.screens.addcity.viewmodel

import android.app.Application
import com.google.android.gms.maps.model.LatLng
import com.msayeh.breeze.domain.model.City
import com.msayeh.breeze.domain.model.Coordinates
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.utils.events.UiEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test


class AddCityViewModelTest {

    private val weatherRepository: WeatherRepository = mockk()
    private val application: Application = mockk()

    private lateinit var viewModel: AddCityViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = AddCityViewModel(weatherRepository, application)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }


    @Test
    fun addMarker_updatesMarkerPositionInState() {
        // Given
        val latLng = LatLng(30.0, 31.0)

        // When
        viewModel.addMarker(latLng)

        // Then
        assertThat(viewModel.uiState.value.markerPosition, `is`(latLng))
    }

    @Test
    fun addMarker_doesNotAffectOtherStateFields() {
        // Given
        val latLng = LatLng(30.0, 31.0)
        val stateBefore = viewModel.uiState.value

        // When
        viewModel.addMarker(latLng)

        // Then
        val stateAfter = viewModel.uiState.value
        assertThat(stateAfter.searchQuery, `is`(stateBefore.searchQuery))
        assertThat(stateAfter.autoCompleteCities, `is`(stateBefore.autoCompleteCities))
    }



    @Test
    fun onAutoCompleteCityClicked_emitsRemoveFocusEvent() = runTest {
        // Given
        val city = City(
            id = 1,
            name = "Cairo",
            country = "EG",
            coordinates = Coordinates(30.0, 31.0),
            isCurrentLocation = false,
            sortOrder = 0
        )
        val events = mutableListOf<UiEvent>()
        val job = launch { viewModel.uiEvent.toList(events) }

        // When
        viewModel.onAutoCompleteCityClicked(city)
        advanceUntilIdle()

        // Then
        assertThat(events, hasItem(UiEvent.RemoveFocus))
        job.cancel()
    }



    @Test
    fun onSearchValueChanged_blankQuery_clearsAutoCompleteCities() = runTest {
        // When
        viewModel.onSearchValueChanged("")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.autoCompleteCities, `is`(emptyList()))
    }

    @Test
    fun onSearchValueChanged_blankQuery_updatesSearchQueryInState() = runTest {
        // When
        viewModel.onSearchValueChanged("")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.searchQuery, `is`(""))
    }

    @Test
    fun onSearchValueChanged_validQuery_updatesSearchQueryInState() = runTest {
        // Given
        coEvery { weatherRepository.getCitySuggestions(any()) } returns Resource.Success(emptyList())

        // When
        viewModel.onSearchValueChanged("Cairo")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.searchQuery, `is`("Cairo"))
    }
}