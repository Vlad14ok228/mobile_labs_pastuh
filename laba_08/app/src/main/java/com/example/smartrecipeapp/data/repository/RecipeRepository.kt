// 1. ПАКЕТ: Файл лежить у папці 'repository', де ми зберігаємо логіку роботи з даними
package com.example.smartrecipeapp.data.repository

// 2. ІМПОРТИ: Підключаємо наші інтерфейси для API та Бази Даних
import com.example.smartrecipeapp.data.api.MealApi
import com.example.smartrecipeapp.data.database.MealDao
import com.example.smartrecipeapp.data.model.Meal
import com.example.smartrecipeapp.data.model.MealResponse

// 3. КЛАС RecipeRepository:
// Це єдине джерело правди (Single Source of Truth) для нашого додатку.
// У конструктор ми передаємо два джерела даних:
// - api: для завантаження свіжих даних з Інтернету.
// - dao: для збереження та читання улюблених рецептів з пам'яті телефону.
class RecipeRepository(
    private val api: MealApi,
    private val dao: MealDao
) {

    // --- РОБОТА З ІНТЕРНЕТОМ (API) ---

    // Функція пошуку. ViewModel викликає її, а Репозиторій переадресовує запит в Retrofit (api).
    // suspend: Виконується у фоновому потоці, щоб не блокувати інтерфейс.
    suspend fun searchMeals(query: String) = api.searchMeals(query)

    // Отримати випадкову страву (для банера "Страва дня").
    suspend fun getRandomMeal() = api.getRandomMeal()

    // 👇 Отримати повну інформацію про страву за її ID.
    // Повертає MealResponse, з якого ми потім витягнемо інструкції, відео тощо.
    suspend fun getMealById(id: String): MealResponse {
        return api.getMealDetails(id)
    }

    // --- РОБОТА З БАЗОЮ ДАНИХ (DAO) ---

    // Отримати список улюблених.
    // Це змінна, а не функція, бо вона повертає Flow (живий потік).
    // Ми просто транслюємо те, що дає нам база даних.
    val favoriteMeals = dao.getAllFavorites()

    // Перевірка: чи лайкнув користувач цей рецепт?
    // Повертає Flow<Boolean>, щоб сердечко зафарбовувалося автоматично.
    fun isFavorite(id: String) = dao.isFavorite(id)

    // Додати в улюблені.
    // Викликаємо метод insertMeal з нашого DAO.
    suspend fun addToFavorites(meal: Meal) = dao.insertMeal(meal)

    // Видалити з улюблених.
    suspend fun removeFromFavorites(meal: Meal) = dao.deleteMeal(meal)
}