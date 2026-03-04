package com.msayeh.breeze.data.network

import com.msayeh.breeze.data.weather.service.WeatherService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val BASE_URL = "https://api.openweathermap.org/"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(PreferencesInterceptor())
        .addInterceptor(ApiKeyInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherService: WeatherService = retrofit.create(WeatherService::class.java)
}