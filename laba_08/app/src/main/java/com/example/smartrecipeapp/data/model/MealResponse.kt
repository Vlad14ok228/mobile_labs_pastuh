// 1. ПАКЕТ: Файл лежить у папці з моделями
package com.example.smartrecipeapp.data.model

// 2. ІМПОРТ: Підключаємо анотацію для парсингу JSON
import com.google.gson.annotations.SerializedName

// 3. КЛАС-ОБГОРТКА (Wrapper):
// Сервер повертає JSON структуру вигляду: { "meals": [ ...список страв... ] }
// Цей клас потрібен, щоб представити цей зовнішній об'єкт { }.
data class MealResponse(

    // 4. @SerializedName("meals"):
    // Ми кажемо бібліотеці Gson: "Знайди в JSON-відповіді ключ 'meals'".
    @SerializedName("meals")

    // 5. ЗМІННА:
    // Сюди запишеться сам список об'єктів Meal.
    // List<Meal>? (Nullable): Знак питання тут критично важливий!
    // Якщо ми шукаємо страву "abrakadabra", сервер не поверне порожній список [],
    // він поверне null. Якщо не поставити '?', програма впаде з помилкою при невдалому пошуку.
    val meals: List<Meal>?
)