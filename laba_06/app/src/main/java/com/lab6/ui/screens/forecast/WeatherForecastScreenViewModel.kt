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
    private val serverModule: ServerApi
) : ViewModel() {

    private val _weatherForecastResponseStateFlow = MutableStateFlow<WeatherForecastResponse?>(null)
    val weatherForecastResponseStateFlow: StateFlow<WeatherForecastResponse?> get() = _weatherForecastResponseStateFlow

    private val _cityName = MutableStateFlow("")
    val cityName: StateFlow<String> get() = _cityName

    fun updateCity(name: String) {
        _cityName.value = name
    }

    fun loadForecast() {
        val city = _cityName.value
        if (city.isBlank()) return
        viewModelScope.launch {
            try {
                val forecast = serverModule.getWeatherForecastByCity(city)
                _weatherForecastResponseStateFlow.value = forecast
            } catch (e: Exception) {
                Log.e("ForecastViewModel", "Error: ${e.message}")
                _weatherForecastResponseStateFlow.value = null
            }
        }
    }
}
