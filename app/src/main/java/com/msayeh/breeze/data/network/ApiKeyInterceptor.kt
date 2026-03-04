package com.msayeh.breeze.data.network

import com.msayeh.breeze.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
        newRequest.url(request.url.newBuilder().addQueryParameter("appid", BuildConfig.API_KEY).build())
        return chain.proceed(newRequest.build())
    }
}