package com.msayeh.breeze.presentation.alerts.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.AlertType
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.PreferencesRepository
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.alerts.receiver.WeatherAlarmReceiver
import com.msayeh.breeze.presentation.alerts.service.AlarmService
import com.msayeh.breeze.presentation.alerts.utils.NotificationUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class WeatherNotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val weatherRepository: WeatherRepository,
    private val prefsRepository: PreferencesRepository,
    private val notificationUtils: NotificationUtils,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val alertId = inputData.getInt(WeatherAlarmReceiver.EXTRA_ALERT_ID, -1)
        Log.d("Alerts", "WeatherNotificationWorker started, alertId: $alertId")
        val alertResource =
            weatherRepository.getAlertById(alertId).takeIf { it is Resource.Success }
                ?: return Result.failure()
        val alert = (alertResource as Resource.Success<Alert>).data

        return try {
            weatherRepository.refreshCurrentWeather(alert.cityId)
            val weather = weatherRepository.observeCityWithWeather(alert.cityId).first()
                ?: return Result.failure()

            if (weather.currentWeather == null) return Result.failure()

            val tempUnit = prefsRepository.getTempUnitFlow().first()
            val title = "${weather.city.name} Current Weather"
            val message =
                "${weather.currentWeather.temperature.format(tempUnit)} - ${appContext.getString(weather.currentWeather.condition.nameResId)}"
            val conditionIcon = weather.currentWeather.condition.iconRes

            when (alert.type) {
                AlertType.NOTIFICATION -> notificationUtils.sendWeatherNotification(
                    alertId,
                    title,
                    message,
                    conditionIcon
                )

                AlertType.ALARM -> startAlarmService(
                    applicationContext,
                    alertId,
                    title,
                    message,
                    conditionIcon
                )
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun startAlarmService(
        context: Context,
        alertId: Int,
        title: String,
        message: String,
        conditionIcon: Int
    ) {
        val intent = AlarmService.createIntent(context, alertId, title, message, conditionIcon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}