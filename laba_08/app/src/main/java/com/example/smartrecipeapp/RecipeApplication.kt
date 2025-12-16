// 1. ПАКЕТ: Коренева папка (або com.example.smartrecipeapp)
package com.example.smartrecipeapp

import android.app.Application
import com.example.smartrecipeapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// 2. КЛАС APPLICATION:
// Це базовий клас всього Android-додатку.
// Він створюється системою при запуску і знищується останнім.
// Ми наслідуємося від нього, щоб додати свою логіку старту (ініціалізацію Koin).
class RecipeApplication : Application() {

    // 3. ON CREATE:
    // Ця функція викликається один раз за все життя програми,
    // як тільки ти натиснув на іконку додатка.
    override fun onCreate() {
        super.onCreate()

        // 4. ЗАПУСК KOIN (Dependency Injection):
        // Ми "вмикаємо" нашу фабрику об'єктів.
        startKoin {
            // а) Передаємо контекст (щоб Koin міг створювати базу даних, яка вимагає Context)
            // this@RecipeApplication посилається на цей самий клас.
            androidContext(this@RecipeApplication)

            // б) Підключаємо наші модулі (інструкції).
            // appModule — це той файл, де ми писали single { ... } і viewModel { ... }.
            modules(appModule)
        }
    }
}