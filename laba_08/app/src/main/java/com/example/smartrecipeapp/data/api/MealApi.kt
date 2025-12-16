// 1. ПАКЕТ: Вказує, де фізично лежить цей файл у проекті.
// Важливо: переконайся, що папка справді називається 'api' (або зміни на 'network', якщо ти назвав її так)
package com.example.smartrecipeapp.data.api

// 2. ІМПОРТИ: Підключаємо класи, які будемо використовувати
import com.example.smartrecipeapp.data.model.MealResponse // Наша модель даних (формат відповіді сервера)
import retrofit2.http.GET   // Анотація для GET-запитів (отримання даних)
import retrofit2.http.Query // Анотація для передачі параметрів у запит (наприклад, ?s=chicken)

// 3. ІНТЕРФЕЙС: Це "меню" можливостей нашого API.
// Retrofit прочитає цей інтерфейс і сам створить код для виконання запитів.
interface MealApi {

    // --- ЗАПИТ 1: Випадкова страва ---
    // @GET каже: "Стукай на адресу base_url/random.php"
    @GET("random.php")
    // suspend fun: Функція, яка може бути призупинена.
    // Це потрібно для Coroutines, щоб запит виконувався у фоні і не "заморожував" екран телефону.
    // Повертає об'єкт MealResponse (список страв).
    suspend fun getRandomMeal(): MealResponse

    // --- ЗАПИТ 2: Пошук страви за назвою ---
    // @GET("search.php") -> стукаємо на search.php
    @GET("search.php")
    // @Query("s") означає, що ми додаємо параметр до посилання.
    // Якщо query = "pie", то посилання стане: .../search.php?s=pie
    suspend fun searchMeals(@Query("s") query: String): MealResponse

    // --- ЗАПИТ 3: Деталі конкретної страви ---
    // @GET("lookup.php") -> стукаємо на lookup.php (пошук за ID)
    @GET("lookup.php")
    // @Query("i") додає ID до запиту.
    // Якщо id = "52772", то посилання стане: .../lookup.php?i=52772
    suspend fun getMealDetails(@Query("i") id: String): MealResponse
}