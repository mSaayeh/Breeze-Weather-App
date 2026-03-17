package com.msayeh.breeze.data.weather.repository

import com.msayeh.breeze.data.weather.local.datasource.WeatherLocalDataSource
import com.msayeh.breeze.data.weather.local.entities.AlertEntity
import com.msayeh.breeze.data.weather.mappers.toDomainModel
import com.msayeh.breeze.data.weather.remote.datasource.WeatherRemoteDataSource
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.presentation.alerts.scheduler.AlertSchedulerManager
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.bouncycastle.util.test.SimpleTest.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private val localDataSource: WeatherLocalDataSource = mockk()
    private val remoteDataSource: WeatherRemoteDataSource = mockk()
    private val schedulerManager: AlertSchedulerManager = mockk()

    private lateinit var repository: WeatherRepositoryImpl

    private val mockAlertEntity: AlertEntity = mockk()
    private val mockAlertCityDetails: Alert = mockk()

    @Before
    fun setup() {
        repository = WeatherRepositoryImpl(remoteDataSource, localDataSource, schedulerManager)
    }


    @Test
    fun removeCityFromFavorites_localDataSourceSucceeds_returnsSuccess() = runTest {
        // Given
        coEvery { localDataSource.deleteCity(1) } just Runs

        // When
        val result = repository.removeCityFromFavorites(1)

        // Then
        assertThat(result, instanceOf(Resource.Success::class.java))
        coVerify(exactly = 1) { localDataSource.deleteCity(1) }
    }

    @Test
    fun removeCityFromFavorites_localDataSourceThrows_returnsError() = runTest {
        // Given
        coEvery { localDataSource.deleteCity(1) } throws Exception("DB Error")

        // When
        val result = repository.removeCityFromFavorites(1)

        // Then
        assertThat(result, instanceOf(Resource.Error::class.java))
        coVerify(exactly = 1) { localDataSource.deleteCity(1) }
    }


    @Test
    fun deleteAlert_alertNotFound_returnsError() = runTest {
        // Given: alert doesn't exist → getAlertById returns null → throws AlertNotFoundException
        coEvery { localDataSource.getAlertById(1) } returns null

        // When
        val result = repository.deleteAlert(1)

        // Then
        assertThat(result, instanceOf(Resource.Error::class.java))
        coVerify(exactly = 0) { schedulerManager.cancel(any()) }
        coVerify(exactly = 0) { localDataSource.deleteAlert(any()) }
    }

    @Test
    fun updateAlertEnabled_alertNotFound_returnsError() = runTest {
        // Given: alert doesn't exist → throws AlertNotFoundException before any update
        coEvery { localDataSource.getAlertById(1) } returns null

        // When
        val result = repository.updateAlertEnabled(1, true)

        // Then
        assertThat(result, instanceOf(Resource.Error::class.java))
        coVerify(exactly = 0) { localDataSource.updateAlertEnabled(any(), any()) }
        coVerify(exactly = 0) { schedulerManager.schedule(any()) }
    }
}
