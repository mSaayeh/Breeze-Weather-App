package com.msayeh.breeze.presentation.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OfflineWeatherRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val weatherRepository: WeatherRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Log.d("OfflineWeatherRefreshWorker", "Starting offline weather refresh...")
        return when(val result = weatherRepository.updateAllCurrentWeatherOnOffline()) {
            is Resource.Error<*> -> {
                Log.d("OfflineWeatherRefreshWorker", "Offline weather refresh failed: ${result.exception}")
                if (runAttemptCount < 3) Result.retry() else Result.failure()
            }
            is Resource.Success<*> -> {
                Log.d("OfflineWeatherRefreshWorker", "Offline weather refresh successful.")
                Result.success()
            }
        }
    }
}