package com.lab6.data.entity.response

import com.lab6.data.entity.Coordinates
import com.lab6.data.entity.WeatherMain

/**
 * WeatherResponse - data class of root response of "/data/2.5/weather" request
 */
data class WeatherResponse(
    val coord: Coordinates,
    val main: WeatherMain,
)
