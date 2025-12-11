package com.lab6.data.entity.response

import com.lab6.data.entity.WeatherForecast

/**
 * WeatherForecastResponse - data class of root response of "/data/2.5/forecast" request
 */
// Це data class, який слугує моделлю для парсингу (читання) відповіді від сервера.
// Він представляє КОРЕНЕВИЙ об'єкт JSON-відповіді.
data class WeatherForecastResponse(

    // Це поле відповідає ключу "list" у JSON, який приходить від OpenWeatherMap.
    // Сервер повертає об'єкт, всередині якого є масив прогнозів під назвою "list".
    // Тому назва змінної тут МАЄ збігатися з назвою в JSON (або використовувати анотацію @SerialName).
    val list: List<WeatherForecast>
    // ^ Це список об'єктів WeatherForecast, кожен з яких містить погоду на конкретний час (кожні 3 години).
)