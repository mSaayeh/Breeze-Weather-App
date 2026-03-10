package com.msayeh.breeze.di

import com.msayeh.breeze.data.weather.local.datasource.WeatherLocalDataSource
import com.msayeh.breeze.data.weather.local.datasource.WeatherLocalDataSourceImpl
import com.msayeh.breeze.data.weather.remote.datasource.WeatherRemoteDataSource
import com.msayeh.breeze.data.weather.remote.datasource.WeatherRemoteDataSourceImpl
import com.msayeh.breeze.data.weather.repository.WeatherRepositoryImpl
import com.msayeh.breeze.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepository: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRemoteDataSource(weatherRemoteDataSource: WeatherRemoteDataSourceImpl): WeatherRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindWeatherLocalDataSource(weatherLocalDataSource: WeatherLocalDataSourceImpl): WeatherLocalDataSource
}