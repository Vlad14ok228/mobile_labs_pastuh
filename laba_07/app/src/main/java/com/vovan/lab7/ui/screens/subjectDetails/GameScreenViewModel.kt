package com.vovan.lab7.ui.screens.subjectDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovan.lab7.data.GeminiAIRepository
import com.vovan.lab7.data.entity.TextPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class GameScreenViewModel(
    // 1. Впровадження Залежностей (DI)
    private val repository: GeminiAIRepository // 👈 Залежність: Репозиторій для спілкування з ШІ, наданий Koin.
) : ViewModel() {
    // ☝️ Клас наслідує ViewModel, щоб його дані (стан) виживали при зміні конфігурації (наприклад, поворот екрана).

    // --- 2. Стан Завантаження ---
    private val _isLoading = MutableStateFlow(false)
    // 👈 MutableStateFlow: Приватний, змінюваний потік даних. Його значення = true, коли ми чекаємо відповіді від ШІ.

    val isLoading: StateFlow<Boolean> get() = _isLoading
    // 👈 StateFlow: Публічний потік. Екран підписується на нього, щоб показувати/ховати індикатор завантаження.

    // --- 3. Стан Списку Питань ---
    private val _quizList = MutableStateFlow<List<TextPair>?>(null)
    // 👈 MutableStateFlow: Зберігає список згенерованих питань/відповідей (List<TextPair>). Спочатку null.

    val quizList: StateFlow<List<TextPair>?> get() = _quizList
    // 👈 StateFlow: Публічний потік. Екран підписується на нього для відображення вікторини.

    // --- 4. Функція Завантаження (Головна Логіка) ---

    fun loadQuiz(topic: String) {
        viewModelScope.launch {
            // 👈 viewModelScope.launch: Виконує асинхронну операцію (запит до ШІ) у фоновому потоці.
            // Це обов'язково, оскільки спілкування з ШІ може тривати кілька секунд.

            _isLoading.value = true // 👈 КРОК 1: Встановлюємо стан "Завантажується" (екран покаже індикатор).

            // КРОК 2: Викликаємо метод Репозиторію для генерації питань ШІ.
            // Результат записуємо у _quizList.
            _quizList.value = repository.generateQuizByTopic(topic)

            _isLoading.value = false // 👈 КРОК 3: Встановлюємо стан "Завантажено" (екран сховає індикатор).
        }
    }
}