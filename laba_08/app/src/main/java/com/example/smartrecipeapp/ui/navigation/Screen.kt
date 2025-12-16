// 1. ПАКЕТ: Папка navigation
package com.example.smartrecipeapp.ui.navigation

// 2. ІМПОРТИ: Нам потрібні іконки для нижнього меню
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// 3. SEALED CLASS (Ізольований клас):
// Це як Enum, але краще. Ми кажемо: "В нашому додатку є ТІЛЬКИ ці екрани і ніяких інших".
// Це гарантує безпеку типів (ми не помилимося в назві екрану десь у коді).
// Параметри:
// - route: Унікальний ID екрану (як URL-адреса сайту). Android використовує це, щоб знайти екран.
// - title: Назва, яку бачить користувач (наприклад, під іконкою в меню).
// - icon: Картинка для меню (може бути null, якщо екран не в меню, як Details).
sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {

    // --- ЕКРАНИ НИЖНЬОГО МЕНЮ (Bottom Navigation) ---
    // Ми використовуємо 'object', бо кожен екран існує в єдиному екземплярі (Singleton).

    // Головна сторінка
    object Home : Screen("home_screen", "Головна", Icons.Default.Home)

    // Сторінка збережених рецептів
    object Favorites : Screen("favorites_screen", "Збережені", Icons.Default.Favorite)

    // Екран з AI (Чат)
    object Chat : Screen("chat_screen", "AI Шеф", Icons.Default.Person)

    // Екран налаштувань
    object Settings : Screen("settings_screen", "Налаштування", Icons.Default.Settings)

    // --- ЕКРАНИ БЕЗ МЕНЮ ---

    // Екран деталей рецепту.
    // Зверни увагу на route: "details_screen/{mealId}"
    // Фігурні дужки {mealId} означають, що це ЗМІННА (аргумент).
    // Це як в інтернеті: youtube.com/watch?v=12345
    object Details : Screen("details_screen/{mealId}", "Рецепт", null) {

        // Допоміжна функція.
        // Коли ми хочемо відкрити рецепт з ID "555", ми викликаємо цю функцію:
        // Screen.Details.createRoute("555") -> поверне рядок "details_screen/555"
        fun createRoute(mealId: String) = "details_screen/$mealId"
    }
}