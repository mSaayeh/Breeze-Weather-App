package com.msayeh.breeze.domain.model

import androidx.annotation.StringRes
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.exception.LocalizedException
import com.msayeh.breeze.domain.exception.UnknownException

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(val exception: LocalizedException, val cachedData: T? = null) :
        Resource<T>()
}

fun <T> tryResource(@StringRes customErrorMessage: Int? = null, block: () -> T): Resource<T> {
    return try {
        Resource.Success(block())
    } catch (e: LocalizedException) {
        e.printStackTrace()
        Resource.Error(e)
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(UnknownException(customErrorMessage ?: R.string.unknown_error))
    }
}

suspend fun <T> tryResourceSuspend(@StringRes customErrorMessage: Int? = null, block: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(block())
    } catch (e: LocalizedException) {
        e.printStackTrace()
        Resource.Error(e)
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(UnknownException(customErrorMessage ?: R.string.unknown_error))
    }
}