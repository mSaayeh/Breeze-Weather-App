package com.msayeh.breeze.domain.exception

import androidx.annotation.StringRes
import com.msayeh.breeze.R

class NoInternetException(@StringRes messageResId: Int = R.string.no_internet_message) :
    LocalizedException(messageResId)