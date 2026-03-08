package com.msayeh.breeze.domain.exception

import androidx.annotation.StringRes

open class LocalizedException(@StringRes val messageResId: Int): Exception()