package com.lab6.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab6.data.entity.WeatherMain

/**
 * WeatherMainCustomView() - custom and reusable compose view for weather data
 * [weatherMain]: WeatherMain - accepts WeatherMain object as parameter
 * - is used on WeatherScreen and WeatherForecastScreen
 */
@Composable
fun WeatherMainCustomView(
    weatherMain: WeatherMain, // 👈 Аргумент 1: Об'єкт даних, який ми маємо відобразити (температура, тиск, вологість).
    modifier: Modifier = Modifier // 👈 Аргумент 2: Модифікатор дозволяє нам налаштовувати вигляд компонента ззовні (розмір, відступи).
) {
    Card(modifier = modifier.padding(16.dp)) { // 👈 Card: Контейнер, який візуально відділяє дані, додаючи тінь і округлені кути.

        Column(modifier = Modifier.padding(6.dp)) { // 👈 Column: Розміщує всі текстові поля вертикально.

            // --- Відображення Температури ---
            Text(
                // Витягуємо значення з об'єкта weatherMain і форматуємо рядок.
                "temperature: ${weatherMain.temp}",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth() // Розтягуємо на всю доступну ширину картки
            )

            // --- Відображення "Відчувається як" ---
            Text(
                "feels like: ${weatherMain.feels_like}",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp) // Додаємо верхній відступ від попереднього елемента
            )

            // --- Відображення Вологості ---
            Text(
                "humidity: ${weatherMain.humidity}",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            // --- Відображення Тиску ---
            Text(
                "pressure: ${weatherMain.pressure}",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }
}

// --- Preview ---

@Preview // 👈 Анотація @Preview дозволяє бачити, як виглядає компонент у вікні дизайну Android Studio.
@Composable
private fun WeatherMainCustomViewPreview() {
    WeatherMainCustomView(
        // Тут ми створюємо "фейкові" дані (мок-дані), щоб показати компонент.
        weatherMain = WeatherMain(
            temp = 322.0,
            feels_like = 321.0,
            pressure = 322,
            humidity = 322
        )
    )
}