package com.example.weatherapp.utils

import androidx.compose.ui.graphics.Color

// Give different background color based on weather code
object ColorUtil {
    fun getBackgroundColor(code: Int): Color {
        return when (code) {
            in 200..299 -> Color.DarkGray // Thunderstorm
            in 300..399 -> Color.Cyan // "Drizzle"
            in 500..599 -> Color.Blue // Rain
            in 600..699 -> Color.White // Snow
            in 700..799 -> Color.Gray // Atmosphere
            800 -> Color.White
            in 801..899 -> Color.Gray // Clouds
            else -> Color.White // Unknown
        }
    }
}