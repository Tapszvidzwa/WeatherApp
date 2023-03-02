package com.example.weatherapp.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.ui.viewmodels.HomeViewModel
import com.example.weatherapp.ui.viewmodels.ScreenState

@Composable
fun HomeScreen(locationData: LocationData) {

    val viewModel: HomeViewModel = hiltViewModel()

    LaunchedEffect(locationData) {
        if (locationData.isAvailable) {
            viewModel.getWeatherDataForCurrentLocation(locationData.latitude, locationData.longitude)
        } else {
            viewModel.retry()
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    // todo: retry on click is not loading again, check it

    when (uiState.screenState) {

        ScreenState.LOADING -> {
            LoadingScreen(loadingMessage = "Fetching latest weather data...")
        }

        ScreenState.SUCCESS -> {
            SuccessScreen(uiState = uiState) { locationName ->
                viewModel.getWeatherData(locationName)
            }
        }

        ScreenState.ERROR -> {
            ErrorScreen(
                locationName = uiState.locationName,
                errorMessage = uiState.errorMsg,
                onRetry = { viewModel.retry() },
                onSearch = { locationName ->
                    viewModel.getWeatherData(locationName)
                }
            )
        }

        ScreenState.IDLE -> {
            LoadingScreen(loadingMessage = "Fetching latest weather data...")
        }
    }
}

data class LocationData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isAvailable: Boolean
)