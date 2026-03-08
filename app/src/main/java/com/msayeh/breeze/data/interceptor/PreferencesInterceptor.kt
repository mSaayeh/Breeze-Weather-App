package com.msayeh.breeze.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class PreferencesInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
        // TODO: Get chosen units and lang from Data Store
        val units = "metric"
        val lang = "en"
        val newUrl = request.url.newBuilder()
            .addQueryParameter("lang", lang)
            .addQueryParameter("units", units)
            .build()
        newRequest.url(newUrl)
        return chain.proceed(newRequest.build())

    }
}