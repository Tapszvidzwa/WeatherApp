package com.example.weatherapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.weatherapp.ui.UiState


@Composable
fun SuccessScreen(uiState: UiState, onSearch: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to uiState.backgroundColor, 1000f to Color.White
                )
            )
    ) {

        SearchBar(locationName = uiState.locationName, onSearch = { cityName ->
            onSearch(cityName)
        })

        Card(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {

                Image(
                    painter = rememberImagePainter(uiState.imageIcon),
                    contentDescription = "Weather icon",
                    Modifier
                        .size(72.dp)
                        .align(CenterHorizontally)
                )

                Text(
                    text = "${uiState.temp}°F",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = uiState.weatherCondition,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Feels Like: ${uiState.feelsLike}°F",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Humidity: ${uiState.humidity}%",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Wind Speed: ${uiState.windSpeed} mph",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}