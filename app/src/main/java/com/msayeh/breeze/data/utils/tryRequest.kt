package com.msayeh.breeze.data.utils

import com.msayeh.breeze.domain.exception.ServerException
import retrofit2.HttpException
import java.io.IOException

suspend fun<T> tryRequest(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: HttpException) {
        e.printStackTrace()
        throw ServerException()
    } catch (e: IOException) {
        e.printStackTrace()
        throw ServerException()
    }
}