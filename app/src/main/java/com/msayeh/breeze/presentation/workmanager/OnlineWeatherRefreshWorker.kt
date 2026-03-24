package com.msayeh.breeze.presentation.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OnlineWeatherRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val weatherRepository: WeatherRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return when(val result = weatherRepository.refreshAllIfStale()) {
            is Resource.Error<*> -> if (runAttemptCount < 3) Result.retry() else Result.failure()
            is Resource.Success<*> -> Result.success()
        }
    }
}