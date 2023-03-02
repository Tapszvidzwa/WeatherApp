package com.example.weatherapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.weatherapp.ui.composables.HomeScreen
import com.example.weatherapp.ui.composables.LoadingScreen
import com.example.weatherapp.ui.composables.LocationData
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), LocationListener {

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWeatherData()
    }

    private fun displayHomeScreen(locationData: LocationData) {
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(locationData = locationData)
                }
            }
        }
    }

    private fun getWeatherData() {
        // Check location permission before displaying weather data
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!PermissionUtils.checkLocationPermission(this)) {
            PermissionUtils.requestLocationPermission(this)
        } else {
            setContent {
                WeatherAppTheme {
                    LoadingScreen(loadingMessage = "Initializing...")
                }
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5f, this)
        }
    }

    // Display weather for current location
    override fun onLocationChanged(location: Location) {
        displayHomeScreen(LocationData(location.latitude, location.longitude, isAvailable = true))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtils.LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherData()
            } else {
                displayHomeScreen(LocationData(isAvailable = false))
            }
        }
    }
}
