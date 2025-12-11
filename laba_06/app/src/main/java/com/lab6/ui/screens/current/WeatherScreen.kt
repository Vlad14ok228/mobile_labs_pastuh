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
    // 1. Отримання ViewModel
    viewModel: WeatherScreenViewModel = koinViewModel()
    // 👈 Koin автоматично надає нам екземпляр ViewModel.
    // ViewModel відповідає за логіку завантаження даних та зберігання стану.
) {
    // 2. Підписка на Стан (Observing)
    val weatherResponseState = viewModel.weatherResponseStateFlow.collectAsState()
    // 👈 Підписуємося на потік відповіді з погодою. Коли ViewModel отримує дані, UI автоматично оновлюється.

    val city by viewModel.cityName.collectAsState()
    // 👈 Підписуємося на поточне значення назви міста, яке зберігається у ViewModel.

    // --- UI: Макет ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Weather by City", fontSize = 22.sp)

        // 3. Поле Введення Міста (Input)
        OutlinedTextField(
            value = city, // 👈 Значення береться зі стану (State) ViewModel
            onValueChange = { viewModel.updateCity(it) },
            // 👈 При зміні тексту ми викликаємо функцію ViewModel, щоб оновити стан назви міста.
            label = { Text("Enter city name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        // 4. Кнопка Завантаження
        Button(
            onClick = { viewModel.loadWeather() },
            // 👈 При натисканні викликаємо головну функцію ViewModel, яка ініціює мережевий запит (Retrofit).
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Get Weather")
        }

        // 5. Відображення Результатів
        // response.value?.let { ... } - Блок виконується тільки, якщо дані вже завантажилися (не null).
        weatherResponseState.value?.let { response ->
            Spacer(modifier = Modifier.height(24.dp))

            // Відображення координат, отриманих з JSON-відповіді
            Text(
                "Coordinates: lat=${response.coord.lat}, lon=${response.coord.lon}",
                fontSize = 16.sp
            )

            // Використовуємо наш перевикористовуваний компонент, передаючи йому дані
            WeatherMainCustomView(weatherMain = response.main)
        }
    }
}