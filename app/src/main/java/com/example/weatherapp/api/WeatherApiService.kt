package com.example.weatherapp.api

import com.example.weatherapp.data.models.geo.GeoModel
import com.example.weatherapp.data.models.geo.LocationDetails
import com.example.weatherapp.data.models.weather.WeatherModel
import io.reactivex.Single
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query

// First 2 calls using suspend calls for coroutines, 1 using rxjava
interface WeatherApiService {

    @GET("data/3.0/onecall")
    suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String,
        @Query("units") units: String = ApiConstants.UNITS_IMPERIAL,
        @Query("appid") apiKey: String = ApiConstants.OPEN_WEATHER_API_KEY
    ): Response<WeatherModel>

    @GET("geo/1.0/direct")
    suspend fun getLocationCoordinates(
        @Query("q") query: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String = ApiConstants.OPEN_WEATHER_API_KEY
    ): Response<GeoModel>

    // This returns Single to demonstrate usage of rxjava observables
    @GET("geo/1.0/reverse")
    fun getCityName(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String = ApiConstants.OPEN_WEATHER_API_KEY
    ): Single<Result<LocationDetails>>
}
