package com.msayeh.breeze.presentation.alerts.receiver

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.msayeh.breeze.domain.model.AlertType
import com.msayeh.breeze.domain.repository.WeatherRepository
import com.msayeh.breeze.presentation.alerts.utils.NotificationUtils
import com.msayeh.breeze.presentation.utils.AlarmPermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmsPermissionChangedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var alarmPermissionManager: AlarmPermissionManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) return
        if (!alarmPermissionManager.canScheduleExactAlarms()) return

        val goAsync = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                weatherRepository.rescheduleAllAlerts(AlertType.ALARM)
            } finally {
                goAsync.finish()
            }
        }
    }
}