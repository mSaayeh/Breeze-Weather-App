package com.msayeh.breeze.presentation.alerts.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.os.PowerManager
import androidx.annotation.DrawableRes
import com.msayeh.breeze.presentation.alerts.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@AndroidEntryPoint
class AlarmService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    @Inject
    lateinit var notificationUtils: NotificationUtils

    private var mediaPlayer: MediaPlayer? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var alertId: Int = -1

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alertId = intent?.getIntExtra(EXTRA_ALERT_ID, -1) ?: -1
        val title = intent?.getStringExtra(EXTRA_TITLE) ?: ""
        val message = intent?.getStringExtra(EXTRA_MESSAGE) ?: ""
        val conditionIcon = intent?.getIntExtra(EXTRA_CONDITION_ICON, -1).takeIf { it != -1 }

        val notification =
            notificationUtils.buildAlarmNotification(alertId, title, message, conditionIcon)

        startForeground(NOTIFICATION_ID, notification)

        acquireWakeLock()
        playSound()
        return START_REDELIVER_INTENT
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmService:WakeLock")
        wakeLock?.acquire(10.minutes.inWholeMilliseconds)
    }

    private fun playSound() {
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(this@AlarmService, ringtoneUri)
            isLooping = true
            prepare()
            start()
        }
    }

    override fun onDestroy() {
        releaseMediaPlayer()
        releaseWakeLock()
        super.onDestroy()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun releaseWakeLock() {
        if (wakeLock?.isHeld == true)
            wakeLock?.release()
        wakeLock = null
    }

    companion object {
        private const val EXTRA_ALERT_ID = "alert_id"
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_MESSAGE = "message"
        private const val EXTRA_CONDITION_ICON = "condition_icon"
        private const val NOTIFICATION_ID = 1001

        fun createIntent(context: Context, alertId: Int, title: String, message: String, @DrawableRes conditionIcon: Int): Intent {
            return Intent(context, AlarmService::class.java).apply {
                putExtra(EXTRA_ALERT_ID, alertId)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_MESSAGE, message)
                putExtra(EXTRA_CONDITION_ICON, conditionIcon)
            }
        }
    }
}