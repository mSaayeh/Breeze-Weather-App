package com.msayeh.breeze.domain.exception

import androidx.annotation.StringRes

open class LocalizedException(@param:StringRes val messageResId: Int): Exception()