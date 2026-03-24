package com.msayeh.breeze.presentation.alerts.scheduler

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.presentation.alerts.receiver.WeatherAlarmReceiver
import com.msayeh.breeze.presentation.alerts.worker.WeatherNotificationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerScheduler @Inject constructor(
    private val workManager: WorkManager,
) {
    fun schedule(alert: Alert) {
        val request = PeriodicWorkRequestBuilder<WeatherNotificationWorker>(
            24, TimeUnit.HOURS
        ).setInitialDelay(calculateDelay(alert.time), TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(WeatherAlarmReceiver.EXTRA_ALERT_ID to alert.id))
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED).build()
            ).build()

        workManager.enqueueUniquePeriodicWork(
            workName(alert.id), ExistingPeriodicWorkPolicy.REPLACE, request
        )
    }

    fun cancel(alertId: Int) {
        workManager.cancelUniqueWork(workName(alertId))
    }

    private fun workName(alertId: Int): String = "weather_notification_$alertId"

    private fun calculateDelay(time: LocalTime): Long {
        val now = LocalDateTime.now()
        var target = LocalDateTime.of(now.toLocalDate(), time)
        if (!target.isAfter(now)) target = target.plusDays(1)

        return target.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - now.atZone(
            ZoneId.systemDefault()
        ).toInstant().toEpochMilli()
    }
}