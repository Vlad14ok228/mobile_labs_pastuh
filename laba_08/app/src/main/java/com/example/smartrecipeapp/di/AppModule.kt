// 1. ПАКЕТ: Папка di (Dependency Injection)
package com.example.smartrecipeapp.di

// 2. ІМПОРТИ:
import androidx.room.Room
import com.example.smartrecipeapp.data.api.MealApi
import com.example.smartrecipeapp.data.database.AppDatabase
import com.example.smartrecipeapp.data.repository.RecipeRepository
import com.example.smartrecipeapp.ui.screens.chat.ChatViewModel
import com.example.smartrecipeapp.ui.screens.details.DetailsViewModel
import com.example.smartrecipeapp.ui.screens.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 3. МОДУЛЬ: Це "креслення" для нашої фабрики.
// Тут ми вчимо Koin, як створювати кожен об'єкт.
val appModule = module {

    // --- БАЗА ДАНИХ ---
    // single: Означає "Одинак" (Singleton).
    // Koin створить цей об'єкт ЛИШЕ ОДИН РАЗ при запуску і буде використовувати його всюди.
    // Чому? Бо підключення до бази - це "важка" операція, не треба створювати 100 баз.
    single {
        Room.databaseBuilder(
            androidContext(), // Отримуємо контекст (Koin сам його знає)
            AppDatabase::class.java,
            "recipe_database" // Ім'я файлу бази на телефоні
        ).build()
    }

    // DAO (Інструмент доступу до даних):
    // get<AppDatabase>() - це магія Koin.
    // Він каже: "Знайди мені вже створену базу даних (з блоку вище) і візьми в неї mealDao".
    single { get<AppDatabase>().mealDao() }

    // --- ІНТЕРНЕТ (RETROFIT) ---
    // single: Retrofit теж важкий об'єкт, тому створюємо його один раз.
    single {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/") // Адреса API
            .addConverterFactory(GsonConverterFactory.create()) // Gson перетворює JSON у Kotlin-об'єкти
            .build()
            .create(MealApi::class.java) // Створюємо реалізацію нашого інтерфейсу MealApi
    }

    // --- РЕПОЗИТОРІЙ ---
    // Це "міст" між даними та екранами.
    // get(), get() - Koin бачить, що Repository хоче (MealApi, MealDao).
    // Він автоматично знаходить їх (бо ми їх оголосили вище як single) і вставляє сюди.
    single { RecipeRepository(get(), get()) }

    // --- VIEWMODELS (ЕКРАНИ) ---
    // viewModel: Це НЕ Singleton!
    // Коли ми відкриваємо екран, Koin створює нову ViewModel.
    // Коли закриваємо екран - ViewModel помирає.

    // Головний екран: потребує Repository (Koin знаходить його через get())
    viewModel { HomeViewModel(get()) }

    // Екран деталей: теж потребує Repository
    viewModel { DetailsViewModel(get()) }

    // AI Чат: не потребує параметрів (у твоїй поточній реалізації), тому дужки пусті.
    viewModel { ChatViewModel() }
}