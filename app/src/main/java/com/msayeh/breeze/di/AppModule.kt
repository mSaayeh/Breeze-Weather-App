package com.msayeh.breeze.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.msayeh.breeze.data.interceptor.ApiKeyInterceptor
import com.msayeh.breeze.data.interceptor.PreferencesInterceptor
import com.msayeh.breeze.data.weather.local.database.WeatherDatabase
import com.msayeh.breeze.data.weather.remote.service.GeoService
import com.msayeh.breeze.data.weather.remote.service.WeatherService
import com.msayeh.breeze.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideOkHttpClient(preferencesRepository: PreferencesRepository): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(PreferencesInterceptor(preferencesRepository))
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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

    @Provides
    @Singleton
    fun provideCityAlertDao(db: WeatherDatabase) = db.cityAlertDao()
}