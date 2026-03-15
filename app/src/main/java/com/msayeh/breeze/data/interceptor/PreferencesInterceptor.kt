package com.msayeh.breeze.data.interceptor

import com.msayeh.breeze.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

class PreferencesInterceptor @Inject constructor(private val prefsRepository: PreferencesRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
        return runBlocking {
            val defaultLocale = Locale.getDefault().language
            val chosenLocale = prefsRepository.getLanguage()?.code
            val newUrl = request.url.newBuilder()
                .addQueryParameter("lang", chosenLocale ?: defaultLocale)
                .addQueryParameter("units", "metric")
                .build()
            newRequest.url(newUrl)
            chain.proceed(newRequest.build())
        }
    }
}