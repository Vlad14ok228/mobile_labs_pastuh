// 1. ПАКЕТ: Папка ui/screens/home
package com.example.smartrecipeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipeapp.data.model.Meal
import com.example.smartrecipeapp.data.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// 2. SEALED INTERFACE (Стани екрану):
// Ми чітко визначаємо три можливі стани інтерфейсу.
// Екран не може бути одночасно "успішним" і "помилковим".
sealed interface HomeUiState {
    data object Loading : HomeUiState               // Спіннер
    data class Success(val meals: List<Meal>) : HomeUiState // Список страв (результат пошуку або рандом)
    data class Error(val message: String) : HomeUiState     // Текст помилки
}

// 3. VIEWMODEL:
// Основний клас логіки. Він живе довше, ніж сам екран (переживає повороти телефону).
// Отримує repository через конструктор (Dependency Injection).
class HomeViewModel(private val repository: RecipeRepository) : ViewModel() {

    // 4. УПРАВЛІННЯ СТАНОМ (StateFlow):
    // _uiState (Mutable) — приватна змінна, яку ми змінюємо тут (всередині ViewModel).
    // Початкове значення = Loading (бо при старті ми відразу вантажимо рандомну страву).
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    // uiState (Immutable) — публічна змінна, яку "слухає" екран (Compose).
    // Екран може тільки читати дані, але не ламати їх.
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // 5. ПОТІК УЛЮБЛЕНИХ СТРАВ:
    // Ця змінна транслює дані прямо з бази даних (через DAO -> Repository).
    // Якщо ти додаси лайк на іншому екрані, цей список оновиться автоматично.
    // (Використовується, якщо ти хочеш показати список улюблених, або просто мати доступ до них).
    val favoriteMeals: Flow<List<Meal>> = repository.favoriteMeals

    // 6. БЛОК INIT:
    // Цей код запускається автоматично, як тільки створюється ViewModel.
    // Тобто, як тільки юзер відкриває додаток — ми відразу шукаємо "Страву дня".
    init {
        loadRandomMeal()
    }

    // --- ЛОГІКА ЗАВАНТАЖЕННЯ (API) ---

    // Функція 1: Отримати випадкову страву
    fun loadRandomMeal() {
        // viewModelScope.launch -> Запускаємо асинхронну операцію (корутину).
        // Це робиться у фоні, щоб не блокувати інтерфейс.
        viewModelScope.launch {
            // 1. Вмикаємо спіннер
            _uiState.value = HomeUiState.Loading
            try {
                // 2. Робимо запит в інтернет
                val response = repository.getRandomMeal()

                // 3. Перевіряємо результат
                if (!response.meals.isNullOrEmpty()) {
                    // Якщо страви є -> показуємо їх (Success)
                    _uiState.value = HomeUiState.Success(response.meals)
                } else {
                    // Якщо пусто -> показуємо помилку
                    _uiState.value = HomeUiState.Error("Нічого не знайдено")
                }
            } catch (e: Exception) {
                // 4. Якщо немає інтернету або сервер впав -> ловимо помилку
                _uiState.value = HomeUiState.Error("Помилка інтернету: ${e.message}")
            }
        }
    }

    // Функція 2: Пошук за назвою (Chicken, Beef...)
    fun searchMeal(query: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val response = repository.searchMeals(query)
                if (!response.meals.isNullOrEmpty()) {
                    _uiState.value = HomeUiState.Success(response.meals)
                } else {
                    _uiState.value = HomeUiState.Error("За запитом '$query' нічого не знайдено")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Помилка пошуку")
            }
        }
    }

    // --- РОБОТА З БАЗОЮ ДАНИХ (Like/Dislike) ---

    // Перевірка: чи лайкнута конкретна страва?
    // Повертає Flow<Boolean>. Це "живий" канал.
    // Екран підписується на нього і сам знає, яким кольором малювати сердечко (червоним чи білим).
    fun isMealFavorite(id: String): Flow<Boolean> = repository.isFavorite(id)

    // Головна кнопка "Сердечко":
    fun toggleFavorite(meal: Meal) {
        viewModelScope.launch {
            // Магія .first():
            // Нам треба знати стан "прямо зараз" (лайкнуто чи ні), а не підписуватися на оновлення.
            // .first() бере одне значення з потоку і зупиняється.
            val isFavorite = repository.isFavorite(meal.id).first()

            if (isFavorite) {
                // Якщо вже є в базі -> видаляємо
                repository.removeFromFavorites(meal)
            } else {
                // Якщо немає -> додаємо
                repository.addToFavorites(meal)
            }
        }
    }
}