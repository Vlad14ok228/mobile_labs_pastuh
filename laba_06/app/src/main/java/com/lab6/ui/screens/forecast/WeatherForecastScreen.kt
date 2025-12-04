package com.lab6.ui.screens.forecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab6.ui.components.WeatherMainCustomView
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherForecastScreen(
    viewModel: WeatherForecastScreenViewModel = koinViewModel()
) {
    val forecastState = viewModel.weatherForecastResponseStateFlow.collectAsState()
    val city by viewModel.cityName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Weather Forecast", fontSize = 22.sp)

        OutlinedTextField(
            value = city,
            onValueChange = { viewModel.updateCity(it) },
            label = { Text("Enter city name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Button(
            onClick = { viewModel.loadForecast() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Get Forecast")
        }

        forecastState.value?.list?.let { forecastList ->
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(forecastList) { forecast ->
                    Text(
                        "Date: ${
                            SimpleDateFormat(
                                "yyyy-MM-dd HH:mm",
                                Locale.getDefault()
                            ).format(Date(forecast.dt * 1000))
                        }",
                        fontSize = 16.sp
                    )
                    WeatherMainCustomView(weatherMain = forecast.main)
                }
            }
        }
    }
}
