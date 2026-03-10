package com.msayeh.breeze.domain.exception

import androidx.annotation.StringRes
import com.msayeh.breeze.R

class CityNotFoundException(@StringRes messageResId: Int = R.string.city_not_found_error): LocalizedException(messageResId)