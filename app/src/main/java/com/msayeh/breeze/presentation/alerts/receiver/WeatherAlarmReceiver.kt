package com.msayeh.breeze.presentation.alerts.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.msayeh.breeze.domain.model.Resource
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.alerts.service.AlarmService
import com.msayeh.breeze.presentation.alerts.worker.WeatherNotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WeatherAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var weatherRepository: WeatherRepository

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_TRIGGER_ALARM -> handleTrigger(context, intent)
            ACTION_DISMISS_ALARM -> handleDismiss(context)
        }
    }

    private fun handleTrigger(context: Context, intent: Intent) {
        val alertId = intent.getIntExtra(EXTRA_ALERT_ID, -1).takeIf { it != -1 } ?: return

        val workRequest = OneTimeWorkRequestBuilder<WeatherNotificationWorker>().setInputData(
                workDataOf(EXTRA_ALERT_ID to alertId)
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED).build()
            ).build()

        workManager.enqueue(workRequest)

        val goAsync = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                weatherRepository.rescheduleAlert(alertId)
            } finally {
                goAsync.finish()
            }
        }

    }

    private fun handleDismiss(context: Context) {
        context.stopService(Intent(context, AlarmService::class.java))
    }

    companion object {
        const val ACTION_DISMISS_ALARM = "dismiss_alarm"
        const val ACTION_TRIGGER_ALARM = "trigger_alarm"
        const val EXTRA_ALERT_ID = "alert_id"
    }
}