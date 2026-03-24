package com.msayeh.breeze.presentation.alerts.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.presentation.alerts.receiver.WeatherAlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationUtils @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManagerCompat,
) {
    fun buildAlarmNotification(
        alertId: Int,
        title: String,
        message: String,
        weatherConditionIcon: Int?
    ) =
        NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setLargeIcon(weatherConditionIcon?.let {
                Icon.createWithResource(
                    context,
                    weatherConditionIcon
                )
            })
            .setOngoing(true)
            .setAutoCancel(false)
            .addAction(
                android.R.drawable.ic_notification_clear_all,
                context.getString(R.string.dismiss), buildDismissPendingIntent(alertId)
            )
            .setDeleteIntent(buildDismissPendingIntent(alertId))
            .build()

    fun sendWeatherNotification(alertId: Int, title: String, message: String, weatherConditionIcon: Int?) {
        if (!notificationManager.areNotificationsEnabled()) return

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setLargeIcon(weatherConditionIcon?.let {
                Icon.createWithResource(
                    context,
                    weatherConditionIcon
                )
            })
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            alertId,
            notification
        )
    }

    fun buildDismissPendingIntent(alertId: Int): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            alertId,
            Intent(context, WeatherAlarmReceiver::class.java).apply {
                action = WeatherAlarmReceiver.ACTION_DISMISS_ALARM
                putExtra(WeatherAlarmReceiver.EXTRA_ALERT_ID, alertId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "weather_notification_channel"
        const val ALARM_CHANNEL_ID = "weather_alarm_channel"
    }
}