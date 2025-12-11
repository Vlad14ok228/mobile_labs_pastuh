package com.lab6.ui.screens.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * MenuScreen - just example of menu screen which leads to the WeatherScreen and WeatherForecastScreen
 */
@Composable
fun MenuScreen(
    // 1. Аргументи (Функції зворотного виклику)
    onWeather: () -> Unit, // 👈 Лямбда-функція: Команда, яка викликається, коли користувач хоче перейти на екран Поточної Погоди.
    onWeatherForecast: () -> Unit // 👈 Лямбда-функція: Команда, яка викликається, коли користувач хоче перейти на екран Прогнозу.
) {
    // Екран не містить жодної логіки чи даних; він лише виконує команди навігації, які йому передають.

    Column( // 👈 Column: Розміщує всі елементи (текст та кнопки) вертикально.
        modifier = Modifier
            .fillMaxSize() // Займає всю доступну область екрана
            .padding(16.dp) // Додає відступи по краях
    ) {
        // Заголовок екрана
        Text("Menu Screen", fontSize = 22.sp, modifier = Modifier.fillMaxWidth())

        // 2. Кнопка "Поточна Погода"
        Button(
            onClick = onWeather, // 👈 При натисканні викликається функція-команда onWeather, що ініціює перехід.
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        ) {
            Text(
                "Weather Screen",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 3. Кнопка "Прогноз Погоди"
        Button(
            onClick = onWeatherForecast, // 👈 При натисканні викликається функція-команда onWeatherForecast.
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                "Weather Forecast Screen",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}