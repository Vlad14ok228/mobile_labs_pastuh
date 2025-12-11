package com.lab6.ui.screens.current

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab6.data.ServerApi
import com.lab6.data.entity.response.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherScreenViewModel(
    // 1. Впровадження Залежностей (DI)
    private val serverModule: ServerApi
) : ViewModel() {
    // ☝️ Клас WeatherScreenViewModel наслідує ViewModel (що дає йому "довге життя").
    // У конструкторі він отримує ServerApi (Retrofit-клієнт), який Koin автоматично йому надає.

    // --- 2. Стан для Результату Погоди ---
    private val _weatherResponseStateFlow = MutableStateFlow<WeatherResponse?>(null)
    // 👈 MutableStateFlow: Приватний, змінюваний потік даних. Тут зберігається результат мережевого запиту.

    val weatherResponseStateFlow: StateFlow<WeatherResponse?> get() = _weatherResponseStateFlow
    // 👈 StateFlow: Публічний, доступний лише для читання потік. Екран (UI) підписується саме на нього.

    // --- 3. Стан для Введеного Міста ---
    private val _cityName = MutableStateFlow("")
    // 👈 MutableStateFlow: Зберігає текст, який користувач вводить у полі.

    val cityName: StateFlow<String> get() = _cityName
    // 👈 StateFlow: Публічний потік, на який підписане поле вводу (OutlinedTextField).

    // --- 4. Функції для Взаємодії з UI ---

    fun updateCity(name: String) {
        _cityName.value = name
        // ☝️ Ця функція викликається при кожному натисканні клавіші в полі вводу.
        // Вона оновлює значення в _cityName, і UI бачить це миттєво.
    }

    fun loadWeather() {
        val city = _cityName.value
        if (city.isBlank()) return // Перевіряємо, чи введено місто. Якщо ні — виходимо.

        // 5. Запуск Мережевого Запиту
        viewModelScope.launch {
            // 👈 viewModelScope: Це вбудована область корутин.
            // Вона гарантує, що запит буде виконаний асинхронно і автоматично скасується,
            // якщо ViewModel буде знищено (наприклад, при виході з екрана).
            try {
                // Виклик методу Retrofit-клієнта (ServerApi).
                val weatherResponse = serverModule.getCurrentWeatherByCity(city)
                // Якщо успіх: записуємо отриманий об'єкт у наш потік стану.
                _weatherResponseStateFlow.value = weatherResponse
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error: ${e.message}")
                // Якщо помилка: очищаємо стан (або відображаємо повідомлення про помилку).
                _weatherResponseStateFlow.value = null
            }
        }
    }
}