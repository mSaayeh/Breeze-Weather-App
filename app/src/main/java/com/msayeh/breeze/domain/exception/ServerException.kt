package com.msayeh.breeze.domain.exception

import androidx.annotation.StringRes
import com.msayeh.breeze.R

class ServerException(@StringRes messageResId: Int = R.string.server_connection_error) :
    LocalizedException(messageResId)