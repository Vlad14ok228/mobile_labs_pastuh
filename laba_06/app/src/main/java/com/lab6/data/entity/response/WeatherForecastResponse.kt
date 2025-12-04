package com.lab6.data.entity.response

import com.lab6.data.entity.WeatherForecast

/**
 * WeatherForecastResponse - data class of root response of "/data/2.5/forecast" request
 */
data class WeatherForecastResponse(
    val list: List<WeatherForecast>
)