package com.lab6.di

import com.lab6.data.ServerApi
import com.lab6.ui.screens.current.WeatherScreenViewModel
import com.lab6.ui.screens.forecast.WeatherForecastScreenViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.core.module.dsl.viewModel

// Base URL of API
private const val BASE_URL = "https://api.openweathermap.org"
// 👈 Приватна константа: Базова адреса API OpenWeatherMap.
// Всі запити (наприклад, /data/2.5/weather) будуть додаватися до цієї адреси.

val appModule = module { // 👈 Блок визначення Koin-модуля. Тут описується, як створювати всі залежності.

    // --- 1. Створення Singleton об'єкта Retrofit ---
    single<ServerApi> { // 👈 Команда 'single': Створює ОДИН (singleton) екземпляр ServerApi на весь додаток.

        val client = OkHttpClient() // Створюємо HTTP-клієнт для виконання мережевих запитів.
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        // 👈 Interceptor: Інструмент для перехоплення запитів. HttpLoggingInterceptor додає детальні логи запитів у Logcat.

        val clientBuilder: OkHttpClient.Builder = client.newBuilder().addInterceptor(interceptor)
        // Додаємо логування до клієнта.

        Retrofit.Builder()
            .baseUrl(BASE_URL) // Встановлюємо базову адресу API.
            .addConverterFactory(GsonConverterFactory.create())
            // 👈 Converter Factory: Вказує Retrofit, що він має використовувати бібліотеку Gson
            // для перетворення JSON-відповідей у наші data-класи (WeatherResponse, WeatherForecastResponse).
            .client(clientBuilder.build()) // Додаємо налаштований клієнт (з логуванням)
            .build()
            .create(ServerApi::class.java)
        // 👈 Створюємо робочий об'єкт ServerApi, який може виконувати запити.
    }
    // ☝️ У результаті, Koin завжди надаватиме один і той самий налаштований об'єкт ServerApi.

    // --- 2. Створення ViewModel ---
    // ViewModel відповідає за логіку екранів. Вона отримує залежності через Koin.

    viewModel { WeatherScreenViewModel(get()) }
    // 👈 Команда 'viewModel': Створює екземпляр ViewModel для поточної погоди.
    // get(): Koin автоматично знаходить та передає сюди об'єкт ServerApi, який ми створили вище.

    viewModel { WeatherForecastScreenViewModel(get()) }
    // 👈 Команда 'viewModel': Створює екземпляр ViewModel для прогнозу погоди, також передаючи йому ServerApi.
}