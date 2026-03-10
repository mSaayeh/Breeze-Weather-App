package com.msayeh.breeze.data.weather.remote.service

import com.msayeh.breeze.data.weather.remote.dto.GeoCityDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoService {
    @GET("geo/1.0/direct")
    fun directGeocoding(@Query("q") query: String, @Query("limit") limit: Int = 5): List<GeoCityDto>

    @GET("geo/1.0/reverse")
    fun reverseGeocoding(@Query("lat") lat: Double, @Query("lon") lon: Double): GeoCityDto
}
