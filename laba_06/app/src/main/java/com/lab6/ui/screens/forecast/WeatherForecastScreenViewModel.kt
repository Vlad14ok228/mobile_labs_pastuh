package com.lab6.ui.screens.forecast

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab6.data.ServerApi
import com.lab6.data.entity.response.WeatherForecastResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherForecastScreenViewModel(
    // 1. Впровадження Залежностей (DI)
    private val serverModule: ServerApi // 👈 Залежність: Retrofit-клієнт, який надається Koin.
) : ViewModel() {
    // ☝️ Клас наслідує ViewModel, щоб його дані (стан) виживали при зміні конфігурації (наприклад, поворот екрана).

    // --- 2. Стан для Результату Прогнозу ---
    private val _weatherForecastResponseStateFlow = MutableStateFlow<WeatherForecastResponse?>(null)
    // 👈 MutableStateFlow: Приватний, змінюваний потік даних. Зберігає об'єкт прогнозу (включно зі списком лаб).

    val weatherForecastResponseStateFlow: StateFlow<WeatherForecastResponse?> get() = _weatherForecastResponseStateFlow
    // 👈 StateFlow: Публічний потік, доступний лише для читання. UI підписується на нього.

    // --- 3. Стан для Введеного Міста ---
    private val _cityName = MutableStateFlow("")
    // 👈 MutableStateFlow: Зберігає текст назви міста, який вводить користувач.

    val cityName: StateFlow<String> get() = _cityName
    // 👈 StateFlow: Публічний потік, на який підписане поле вводу на екрані.

    // --- 4. Функції для Взаємодії з UI ---

    fun updateCity(name: String) {
        _cityName.value = name
        // ☝️ Оновлює стан назви міста. Це викликає перемальовування поля вводу.
    }

    fun loadForecast() {
        val city = _cityName.value
        if (city.isBlank()) return // Захист: якщо місто не введено, запит не виконуємо.

        // 5. Запуск Мережевого Запиту
        viewModelScope.launch {
            // 👈 viewModelScope.launch: Виконує асинхронну операцію (мережевий запит) у фоновому потоці.
            // Це обов'язково для Retrofit, щоб не блокувати головний UI-потік.
            try {
                // Виклик методу Retrofit-клієнта (ServerApi) для отримання прогнозу.
                val forecast = serverModule.getWeatherForecastByCity(city)
                // Якщо успіх: записуємо отриманий об'єкт у наш потік стану. UI оновлюється автоматично.
                _weatherForecastResponseStateFlow.value = forecast
            } catch (e: Exception) {
                // Обробка помилок (наприклад, місто не знайдено, немає інтернету).
                Log.e("ForecastViewModel", "Error: ${e.message}")
                _weatherForecastResponseStateFlow.value = null
            }
        }
    }
}