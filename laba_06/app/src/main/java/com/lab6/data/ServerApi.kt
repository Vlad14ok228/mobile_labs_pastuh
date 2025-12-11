package com.lab6.data

import com.lab6.data.entity.response.WeatherForecastResponse
import com.lab6.data.entity.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi { // 👈 Інтерфейс ServerApi: Це контракт, який визначає, як ми спілкуємося з API.

    @GET("/data/2.5/weather") // 👈 Анотація @GET: Позначає, що це HTTP-запит на отримання даних.
    // "/data/2.5/weather": Це частина URL-адреси, яка додається до базового посилання (https://api.openweathermap.org/...).
    // Це адреса для отримання поточної погоди.
    suspend fun getCurrentWeatherByCity(

        @Query("q") city: String, // 👈 @Query: Параметр, який додається до URL після знака '?'.
        // "q": назва ключа, "city": змінна, яку ми передаємо (наприклад, ?q=Lviv). Це назва міста.

        @Query("appid") apiId: String = "8889c1feba30ba15f018e6919a6bc4e2",
        // 👈 @Query: Унікальний ключ доступу (API ID). Його треба передавати завжди.

        @Query("units") units: String = "metric",
        // 👈 @Query: Параметр для одиниць вимірювання. "metric" = Цельсій (°C), "imperial" = Фаренгейт (°F).

    ): WeatherResponse
    // ☝️ Повертає об'єкт WeatherResponse. Retrofit автоматично перетворює отриманий JSON у наш Kotlin data class.

    // --- Другий запит: Прогноз погоди на декілька днів ---

    @GET("/data/2.5/forecast") // 👈 Адреса для багатоденного прогнозу (прогноз на 5 днів).
    suspend fun getWeatherForecastByCity(

        @Query("q") city: String, // 👈 Назва міста
        @Query("appid") apiId: String = "8889c1feba30ba15f018e6919a6bc4e2", // 👈 API ID
        @Query("units") units: String = "metric", // 👈 Одиниці вимірювання

    ): WeatherForecastResponse
    // ☝️ Повертає об'єкт WeatherForecastResponse, який містить List<WeatherForecast>.
}