package com.vovan.lab7.di

import com.vovan.lab7.data.GeminiAIRepository
import com.vovan.lab7.ui.screens.subjectDetails.GameScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module { // 👈 Блок визначення Koin-модуля. Тут ми описуємо, як створювати об'єкти.

    // --- 1. Створення Gemini AI Репозиторію ---
    single<GeminiAIRepository> { GeminiAIRepository() }
    // 👈 Команда 'single': Створює ОДИН (singleton) екземпляр GeminiAIRepository на весь додаток.
    // Навіщо single? Щоб уникнути багаторазової ініціалізації моделі ШІ та забезпечити єдину точку доступу до API.

    // --- 2. Створення ViewModel ---
    viewModel { GameScreenViewModel(get()) }
    // 👈 Команда 'viewModel': Створює екземпляр ViewModel для екрана гри.
    // get(): Koin автоматично знаходить та передає сюди створений вище об'єкт GeminiAIRepository.
    // ViewModel тепер може використовувати функції Репозиторію (generateQuizByTopic) для роботи з ШІ.
}