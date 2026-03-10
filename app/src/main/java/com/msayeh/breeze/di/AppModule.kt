package com.msayeh.breeze.di

import android.app.Application
import androidx.room.Room
import com.msayeh.breeze.data.interceptor.ApiKeyInterceptor
import com.msayeh.breeze.data.interceptor.PreferencesInterceptor
import com.msayeh.breeze.data.weather.local.database.WeatherDatabase
import com.msayeh.breeze.data.weather.remote.service.GeoService
import com.msayeh.breeze.data.weather.remote.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(PreferencesInterceptor())
            .addInterceptor(ApiKeyInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    fun provideGeoService(retrofit: Retrofit): GeoService =
        retrofit.create(GeoService::class.java)

    @Provides
    @Singleton
    fun provideWeatherDatabase(app: Application): WeatherDatabase =
        Room.databaseBuilder(
            app,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration(true).build()

    @Provides
    @Singleton
    fun provideCityDao(db: WeatherDatabase) = db.cityDao()

    @Provides
    @Singleton
    fun provideWeatherDao(db: WeatherDatabase) = db.weatherDao()

    @Provides
    @Singleton
    fun provideCityWeatherDao(db: WeatherDatabase) = db.cityWeatherDao()
}