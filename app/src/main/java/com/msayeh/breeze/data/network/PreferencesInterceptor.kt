package com.msayeh.breeze.data.network

import okhttp3.Interceptor
import okhttp3.Response

class PreferencesInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
        // TODO: Get chosen units and lang from Data Store
        val units = "metric"
        val lang = "ar"
        newRequest.url(request.url.newBuilder().addQueryParameter("units", units).build())
        newRequest.url(request.url.newBuilder().addQueryParameter("lang", lang).build())
        return chain.proceed(newRequest.build())

    }
}