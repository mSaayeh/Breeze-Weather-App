package com.msayeh.breeze.presentation.workmanager

import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRefreshScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    companion object {
        private const val ONLINE_WORK_NAME = "online_weather_periodic_refresh"
        private const val OFFLINE_WORK_NAME = "offline_weather_periodic_refresh"
    }

    fun schedule() {
        scheduleOnlineWorker()
        scheduleOfflineWorker()
    }

    private fun scheduleOnlineWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<OnlineWeatherRefreshWorker>(
            30, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            ONLINE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun scheduleOfflineWorker() {
        Log.d("OfflineWeatherRefreshWorker", "Scheduling offline weather refresh worker...")
        val request = PeriodicWorkRequestBuilder<OfflineWeatherRefreshWorker>(
            30, TimeUnit.MINUTES
        )
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            OFFLINE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun cancel() {
        workManager.cancelUniqueWork(ONLINE_WORK_NAME)
        workManager.cancelUniqueWork(OFFLINE_WORK_NAME)
    }

    fun getOnlineWorkInfoFlow(): Flow<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkFlow(ONLINE_WORK_NAME)

    fun getOfflineWorkInfoFlow(): Flow<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkFlow(OFFLINE_WORK_NAME)
}