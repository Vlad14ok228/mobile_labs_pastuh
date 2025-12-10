package com.lab4.di

import android.content.Context
import androidx.room.Room
import com.lab4.data.db.Lab4Database
import com.lab4.ui.screens.subjectDetails.SubjectDetailsViewModel
import com.lab4.ui.screens.subjectsList.SubjectsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * appModule = module{...} - is Koin module for creating instances of all components in App
 * - invokes in App class
 * - in the module{...} scope you can create different instances by functions single{}, factory{}, viewModel{}
 * - in the module{...} scope to get some other instance which was created in scope you can call get()
 */
val appModule = module { // 👈 Блок визначення Koin-модуля: тут ми описуємо, як створювати об'єкти

    single<Lab4Database> { // 👈 Команда 'single': Створює ОДИН (singleton) екземпляр класу на весь час роботи додатка
        Room.databaseBuilder( // 👈 Створення об'єкта бази даних Room
            get<Context>(), // 👈 get<Context>(): Koin автоматично надає Context, який потрібен для створення бази
            Lab4Database::class.java, "lab4Database"
        ).build()
    }
    // ☝️ У результаті, ми маємо єдиний, доступний звідусіль об'єкт Lab4Database.

    // --- Створення ViewModel ---
    // ViewModel використовується для зберігання логіки та даних, які "переживають" зміну конфігурації екрана (наприклад, поворот).

    viewModel { SubjectsListViewModel(get()) }
    // ☝️ Команда 'viewModel': Створює екземпляр SubjectsListViewModel, який буде жити стільки, скільки живе екран.
    // get(): Koin автоматично знаходить і передає сюди об'єкт Lab4Database, який ми створили вище.

    viewModel { SubjectDetailsViewModel(get()) }
    // ☝️ Створює екземпляр SubjectDetailsViewModel, також передаючи йому об'єкт Lab4Database.
}