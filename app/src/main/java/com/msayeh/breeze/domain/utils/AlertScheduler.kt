package com.msayeh.breeze.domain.utils

import com.msayeh.breeze.domain.model.Alert

interface AlertScheduler {
    fun schedule(alert: Alert)
    fun cancel(alert: Alert)
}