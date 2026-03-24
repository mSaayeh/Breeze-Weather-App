package com.msayeh.breeze.presentation.alerts.scheduler

import com.msayeh.breeze.domain.model.Alert
import com.msayeh.breeze.domain.model.AlertType
import com.msayeh.breeze.domain.utils.AlertScheduler
import javax.inject.Inject
import javax.inject.Singleton

class AlertSchedulerManager @Inject constructor(
    private val alarmManagerScheduler: AlarmManagerScheduler,
    private val workManagerScheduler: WorkManagerScheduler
): AlertScheduler {

    override fun schedule(alert: Alert) {
        when(alert.type) {
            AlertType.NOTIFICATION -> workManagerScheduler.schedule(alert)
            AlertType.ALARM -> alarmManagerScheduler.schedule(alert)
        }
    }

    override fun cancel(alert: Alert) {
        when(alert.type) {
            AlertType.NOTIFICATION -> workManagerScheduler.cancel(alert.id)
            AlertType.ALARM -> alarmManagerScheduler.cancel(alert.id)
        }
    }
}