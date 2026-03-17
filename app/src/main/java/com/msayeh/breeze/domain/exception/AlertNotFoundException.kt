package com.msayeh.breeze.domain.exception

import androidx.annotation.StringRes
import com.msayeh.breeze.R

class AlertNotFoundException(@StringRes messageResId: Int = R.string.alarm_not_found_error): LocalizedException(messageResId)