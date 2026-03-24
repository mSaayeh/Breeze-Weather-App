package com.msayeh.breeze.presentation.utils

import android.app.AlarmManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmPermissionManager @Inject constructor(
    private val alarmManager: AlarmManager
) {
    fun canScheduleExactAlarms(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}