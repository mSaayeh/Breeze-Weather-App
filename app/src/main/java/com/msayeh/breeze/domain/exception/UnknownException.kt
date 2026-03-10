package com.msayeh.breeze.domain.exception

import androidx.annotation.StringRes
import com.msayeh.breeze.R

class UnknownException(@StringRes messageResId: Int = R.string.unknown_error) :
    LocalizedException(messageResId)