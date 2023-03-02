package com.example.weatherapp.data.repository

import com.example.weatherapp.api.NetworkResult
import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.data.models.geo.GeoModel
import com.example.weatherapp.data.models.geo.LocationDetails
import com.example.weatherapp.data.models.weather.WeatherModel
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject

// Handles Api calls as well as caching location
class WeatherDataRepository @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val locationCache: LocationNameCache
) {
    fun updateLocationName(cityName: String) {
        locationCache.saveLocationName(cityName)
    }

    fun getLastSavedLocation(): String {
        return locationCache.getLocationName() ?: ""
    }

    suspend fun getWeatherData(lat: Double, lon: Double): NetworkResult<WeatherModel> {
        try {
            val result = weatherApiService.getWeatherData(
                latitude = lat,
                longitude = lon,
                exclude = ""
            )

            return if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(data = result.body()!!)
            } else {
                NetworkResult.Error(message = result.errorBody().toString())
            }

        } catch (httpException: HttpException) {
            return NetworkResult.Error(message = "Network error occured")
        } catch (exception: Exception) {
            return NetworkResult.Error(message = "An error occured: " + exception.message.toString())
        }
    }

    suspend fun getGeoCoordinates(city: String): NetworkResult<GeoModel> {
        try {
            val result = weatherApiService.getLocationCoordinates(
                query = city
            )

            return if (result.isSuccessful && !result.body().isNullOrEmpty()) {
                NetworkResult.Success(data = result.body()!!)
            } else {
                NetworkResult.Error(message = "Please enter valid location name")
            }

        } catch (httpException: HttpException) {
            return NetworkResult.Error(message = "Network error occured, please try again later")
        } catch (exception: Exception) {
            return NetworkResult.Error(message = "An error occured, please retry")
        }
    }

    fun getLocationName(lat: Double, lon: Double): Single<Result<LocationDetails>> {
        return weatherApiService.getCityName(
            latitude = lat,
            longitude = lon
        )
    }
}

