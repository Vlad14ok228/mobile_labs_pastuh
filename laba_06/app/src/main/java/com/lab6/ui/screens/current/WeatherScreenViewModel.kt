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
    private val serverModule: ServerApi
) : ViewModel() {

    private val _weatherResponseStateFlow = MutableStateFlow<WeatherResponse?>(null)
    val weatherResponseStateFlow: StateFlow<WeatherResponse?> get() = _weatherResponseStateFlow

    private val _cityName = MutableStateFlow("")
    val cityName: StateFlow<String> get() = _cityName

    fun updateCity(name: String) {
        _cityName.value = name
    }

    fun loadWeather() {
        val city = _cityName.value
        if (city.isBlank()) return
        viewModelScope.launch {
            try {
                val weatherResponse = serverModule.getCurrentWeatherByCity(city)
                _weatherResponseStateFlow.value = weatherResponse
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error: ${e.message}")
                _weatherResponseStateFlow.value = null
            }
        }
    }
}
