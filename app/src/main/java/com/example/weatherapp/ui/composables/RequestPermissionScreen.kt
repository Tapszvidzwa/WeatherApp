package com.example.weatherapp.ui.composables

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

@Composable
fun RequestPermissionsScreen(
    getWeatherDataForCurrentLocation: () -> Unit,
    getWeatherForLastSavedLocation: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getWeatherDataForCurrentLocation.invoke()
        } else {
            getWeatherForLastSavedLocation.invoke()
        }
    }

    SideEffect {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}