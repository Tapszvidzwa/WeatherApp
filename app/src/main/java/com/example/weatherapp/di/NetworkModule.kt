package com.example.weatherapp.di

import android.net.Uri
import com.example.weatherapp.api.ApiConstants
import com.example.weatherapp.api.HttpClient
import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.data.models.UriTypeAdapter
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkService(): WeatherApiService {
        // Could also add configs for connection time outs as improvement
        return Retrofit.Builder()
            .baseUrl(ApiConstants.OPEN_WEATHER_BASE_URL)
            .client(HttpClient.client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
                        .create()
                )
            )
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}