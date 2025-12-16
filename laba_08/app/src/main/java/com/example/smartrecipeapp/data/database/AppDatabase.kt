// 1. ПАКЕТ: Місце, де лежить файл. У твоїй новій структурі це папка data/database
package com.example.smartrecipeapp.data.database

// 2. ІМПОРТИ: Підключаємо бібліотеку Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartrecipeapp.data.model.Meal

// 3. АНОТАЦІЯ @Database: Це головні налаштування бази.
// entities = [Meal::class] -> Список таблиць. Ми кажемо: "Створи таблицю на основі класу Meal".
// version = 1 -> Версія бази. Якщо ти колись додаси нове поле в Meal, треба буде змінити на 2.
@Database(entities = [Meal::class], version = 1)

// 4. КЛАС: Це "вхідні двері" до бази даних.
// abstract class -> Клас ОБОВ'ЯЗКОВО має бути абстрактним.
// Чому? Бо Room сам згенерує весь складний код "під капотом", нам не треба писати реалізацію.
// extends RoomDatabase -> Наслідуємося від стандартного класу Room.
abstract class AppDatabase : RoomDatabase() {

    // 5. DAO (Data Access Object): Метод для отримання інструменту роботи з даними.
    // Ми не пишемо код всередині (тому функція abstract).
    // Room сам зрозуміє, що треба повернути згенерований клас MealDao_Impl.
    // Через цей метод ми будемо додавати, видаляти та читати рецепти.
    abstract fun mealDao(): MealDao
}