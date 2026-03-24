package com.msayeh.breeze.presentation.alerts.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.msayeh.breeze.domain.repository.WeatherRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    @Inject lateinit var weatherRepository: WeatherRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        val goAsync = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                weatherRepository.rescheduleAllAlerts()
            } finally {
                goAsync.finish()
            }
        }
    }
}