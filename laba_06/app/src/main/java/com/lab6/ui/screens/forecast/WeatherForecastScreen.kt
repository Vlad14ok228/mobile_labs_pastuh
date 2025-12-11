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
    // 1. Отримання ViewModel
    viewModel: WeatherForecastScreenViewModel = koinViewModel()
    // 👈 Koin автоматично надає нам екземпляр ViewModel для прогнозу.
    // ViewModel відповідає за логіку завантаження даних та зберігання стану (місто, результат).
) {
    // 2. Підписка на Стан (Observing)
    val forecastState = viewModel.weatherForecastResponseStateFlow.collectAsState()
    // 👈 Підписуємося на потік, що містить об'єкт WeatherForecastResponse (список прогнозів).

    val city by viewModel.cityName.collectAsState()
    // 👈 Підписуємося на назву міста, що зберігається у ViewModel.

    // --- UI: Макет ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Weather Forecast", fontSize = 22.sp)

        // 3. Поле Введення Міста (Input)
        OutlinedTextField(
            value = city, // 👈 Значення береться зі стану ViewModel
            onValueChange = { viewModel.updateCity(it) },
            // 👈 При введенні тексту оновлюємо стан назви міста у ViewModel.
            label = { Text("Enter city name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        // 4. Кнопка Завантаження
        Button(
            onClick = { viewModel.loadForecast() },
            // 👈 При натисканні викликаємо функцію ViewModel, яка ініціює мережевий запит
            // на отримання багатоденного прогнозу (Retrofit).
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Get Forecast")
        }

        // 5. Відображення Списку Прогнозів
        // forecastState.value?.list?.let { ... } - Блок виконується лише, якщо дані успішно завантажені
        // і сам список прогнозів ('list') не є порожнім.
        forecastState.value?.list?.let { forecastList ->
            // LazyColumn - Оптимізований список для відображення великої кількості записів прогнозу.
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(forecastList) { forecast -> // Цикл по кожному запису прогнозу (кожні 3 години)
                    Text(
                        // --- ВАЖЛИВО: Конвертація UNIX-часу ---
                        "Date: ${
                            SimpleDateFormat(
                                "yyyy-MM-dd HH:mm",
                                Locale.getDefault()
                            ).format(Date(forecast.dt * 1000))
                        }",
                        // ☝️ forecast.dt - це UNIX-час (секунди). Ми множимо його на 1000, щоб отримати мілісекунди,
                        // і конвертуємо у зручний для людини формат дати та часу (SimpleDateFormat).
                        fontSize = 16.sp
                    )
                    // Використовуємо наш багаторазовий компонент для відображення температури, тиску, тощо.
                    WeatherMainCustomView(weatherMain = forecast.main)
                }
            }
        }
    }
}