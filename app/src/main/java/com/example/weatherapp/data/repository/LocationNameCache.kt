package com.example.weatherapp.data.repository

import android.content.Context
import javax.inject.Inject

class LocationNameCache @Inject constructor(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(CITY_PREFS, Context.MODE_PRIVATE)

    fun saveLocationName(cityName: String) {
        sharedPreferences.edit().putString(CITY_NAME, cityName).apply()
    }

    fun getLocationName(): String? {
        return sharedPreferences.getString(CITY_NAME, null)
    }

    companion object {
        private const val CITY_PREFS = "city_prefs"
        private const val CITY_NAME = "city_name"
    }
}
