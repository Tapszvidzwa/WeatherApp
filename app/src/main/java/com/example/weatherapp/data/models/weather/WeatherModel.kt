package com.example.weatherapp.data.models.weather

import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @SerializedName("alerts") val alerts: List<Alert> = emptyList(),
    @SerializedName("current") val current: Current? = null,
    @SerializedName("daily") val daily: List<Daily> = emptyList()
//    val hourly: List<Hourly>,
//    val lat: Double,
//    val lon: Double,
//    val minutely: List<Minutely>,
//    val timezone: String,
//    val timezone_offset: Int
)