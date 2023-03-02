package com.example.weatherapp.data.models.geo

import com.google.gson.annotations.SerializedName

data class LocationDetailsItem(
    @SerializedName("country") val country: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("local_names") val local_names: LocalNamesX,
    @SerializedName("lon") val lon: Double,
    @SerializedName("name") val name: String,
    @SerializedName("state") val state: String
)