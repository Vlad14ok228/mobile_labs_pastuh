package com.lab6.ui.screens.current

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab6.ui.components.WeatherMainCustomView
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherScreenViewModel = koinViewModel()
) {
    val weatherResponseState = viewModel.weatherResponseStateFlow.collectAsState()
    val city by viewModel.cityName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Weather by City", fontSize = 22.sp)

        OutlinedTextField(
            value = city,
            onValueChange = { viewModel.updateCity(it) },
            label = { Text("Enter city name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Button(
            onClick = { viewModel.loadWeather() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Get Weather")
        }

        weatherResponseState.value?.let { response ->
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Coordinates: lat=${response.coord.lat}, lon=${response.coord.lon}",
                fontSize = 16.sp
            )
            WeatherMainCustomView(weatherMain = response.main)
        }
    }
}
