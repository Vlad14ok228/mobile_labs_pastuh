// 1. ПАКЕТ: Папка ui/screens/details
package com.example.smartrecipeapp.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipeapp.data.model.Meal
import com.example.smartrecipeapp.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// 2. SEALED INTERFACE (Ізольований інтерфейс станів):
// Це "світлофор" для нашого екрану. Екран може бути ТІЛЬКИ в одному з цих 3 станів.
// Це краще, ніж мати окремі змінні isLoading, isError, isSuccess, бо стани не можуть перекриватися.
sealed interface DetailsUiState {
    data object Loading : DetailsUiState               // Крутилка
    data class Success(val meal: Meal) : DetailsUiState // Показуємо рецепт
    data class Error(val message: String) : DetailsUiState // Показуємо текст помилки
}

// 3. VIEWMODEL:
// Приймає repository через конструктор (дякуючи Koin).
class DetailsViewModel(private val repository: RecipeRepository) : ViewModel() {

    // 4. STATEFLOW (Потік стану):
    // _uiState (Mutable) - приватна змінна, ми її змінюємо всередині цього класу.
    // Початковий стан = Loading (поки не завантажили дані).
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)

    // uiState (Immutable) - публічна змінна, екран може її тільки читати (підписатися).
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    // 5. ЗАВАНТАЖЕННЯ РЕЦЕПТУ:
    // Викликається з DetailsScreen через LaunchedEffect при вході.
    fun loadMeal(id: String) {
        // viewModelScope.launch -> Запускаємо у фоновому потоці.
        // Якщо юзер закриє екран до того, як дані прийдуть, цей процес автоматично скасується.
        viewModelScope.launch {
            // Спочатку показуємо крутилку
            _uiState.value = DetailsUiState.Loading
            try {
                // Робимо запит до API через репозиторій
                val response = repository.getMealById(id)

                // API повертає список (List), навіть якщо страва одна.
                // firstOrNull() бере перший елемент або null, якщо список пустий.
                val meal = response.meals?.firstOrNull()

                if (meal != null) {
                    // Успіх! Передаємо страву в UI
                    _uiState.value = DetailsUiState.Success(meal)
                } else {
                    // Список пустий (дивний ID)
                    _uiState.value = DetailsUiState.Error("Рецепт не знайдено")
                }
            } catch (e: Exception) {
                // Помилка інтернету або сервера
                _uiState.value = DetailsUiState.Error("Помилка завантаження: ${e.message}")
            }
        }
    }

    // 6. ПЕРЕВІРКА НА ЛАЙК:
    // Просто повертає Flow з бази даних.
    // Екран підписується на це і сам оновлює іконку ❤️.
    fun isMealFavorite(id: String) = repository.isFavorite(id)

    // 7. ПЕРЕМИКАЧ ЛАЙКА:
    fun toggleFavorite(meal: Meal) {
        viewModelScope.launch {
            // repository.isFavorite(meal.id) повертає потік (Flow).
            // .first() означає: "Візьми ТІЛЬКИ ПОТОЧНЕ значення (true/false) і відпишись".
            // Нам не треба слідкувати вічно, нам треба знати стан прямо зараз.
            val isFav = repository.isFavorite(meal.id).first()

            if (isFav) {
                // Якщо вже лайкнуто -> видаляємо
                repository.removeFromFavorites(meal)
            } else {
                // Якщо не лайкнуто -> додаємо
                repository.addToFavorites(meal)
            }
        }
    }
}