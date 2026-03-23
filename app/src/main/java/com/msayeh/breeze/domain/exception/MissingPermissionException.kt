package com.msayeh.breeze.domain.exception

import androidx.annotation.StringRes

class MissingPermissionException(@StringRes messageResId: Int) : LocalizedException(messageResId)