package com.msayeh.breeze.domain.model

import com.msayeh.breeze.domain.exception.LocalizedException

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Loading<T>(val cachedData: T? = null) : Resource<T>()
    data class Error<T>(val exception: LocalizedException, val cachedData: T? = null) : Resource<T>()
}