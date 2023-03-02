package com.example.weatherapp.ui

import androidx.compose.ui.graphics.Color
import com.example.weatherapp.ui.viewmodels.ScreenState

data class UiState(
    val errorMsg: String = "",
    val weatherCondition: String = "",
    val temp: Double = 0.0,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val feelsLike: Double = 0.0,
    val imageIcon: String = "",
    val locationName: String = "",
    val backgroundColor: Color = Color.White,
    val screenState: ScreenState = ScreenState.IDLE
)
