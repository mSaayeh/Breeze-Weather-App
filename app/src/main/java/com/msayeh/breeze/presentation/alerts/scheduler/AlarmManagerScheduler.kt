package com.msayeh.breeze.presentation.alerts.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.presentation.alerts.receiver.WeatherAlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmManagerScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager,
) {
    fun schedule(alert: Alert) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!alarmManager.canScheduleExactAlarms()) return
        }
        Log.d("Alerts", "Scheduling alarm for alert ${alert.id} at ${alert.time}")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calculateTriggerTime(alert.time),
            buildPendingIntent(alert.id)
        )
    }

    fun cancel(alertId: Int) {
        alarmManager.cancel(buildPendingIntent(alertId))
    }

    private fun calculateTriggerTime(time: LocalTime): Long {
        val now = LocalDateTime.now()
        var target = LocalDateTime.of(LocalDate.now(), time)
        if (!target.isAfter(now)) target = target.plusDays(1)

        return target.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun buildPendingIntent(alertId: Int): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            alertId,
            Intent(context, WeatherAlarmReceiver::class.java).apply {
                action = WeatherAlarmReceiver.ACTION_TRIGGER_ALARM
                putExtra(WeatherAlarmReceiver.EXTRA_ALERT_ID, alertId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}