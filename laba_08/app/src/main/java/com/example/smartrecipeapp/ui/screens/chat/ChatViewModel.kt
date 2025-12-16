// 1. ПАКЕТ ТА ІМПОРТИ
package com.example.smartrecipeapp.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipeapp.data.model.ChatMessage
import com.google.firebase.Firebase
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 2. КЛАС ViewModel:
// Цей клас переживає повороти екрану. Якщо ти перевернеш телефон,
// історія чату не зникне, бо вона зберігається тут, а не в Activity.
class ChatViewModel : ViewModel() {

    // 3. ІНІЦІАЛІЗАЦІЯ AI (Vertex AI in Firebase):
    // Ми створюємо об'єкт generativeModel. Це наш доступ до "розуму" Gemini.
    // "gemini-2.5-flash" — це назва моделі. Flash означає, що вона швидка і дешева (ідеально для чату).
    private val generativeModel = Firebase.vertexAI.generativeModel("gemini-2.5-flash")

    // 4. СТАН ЧАТУ (StateFlow):
    // _messages (Mutable) — це "чорновик", який ми можемо змінювати (додавати повідомлення).
    // Він приватний, щоб ніхто ззовні не зламав історію.
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())

    // messages (Immutable) — це "чистовик" для екрану (UI).
    // Екран може тільки ЧИТАТИ цей список, але не змінювати його напряму.
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // 5. СТАН ЗАВАНТАЖЕННЯ:
    // true = показуємо спіннер (робот думає), false = ховаємо.
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 6. БЛОК init:
    // Виконується один раз при створенні ViewModel.
    // Ми відразу додаємо привітання від бота, щоб екран не був пустим.
    init {
        addMessage(ChatMessage("Привіт! Я твій AI Шеф. Що будемо готувати?", isUser = false))
    }

    // 7. ФУНКЦІЯ ВІДПРАВКИ (Головна логіка):
    fun sendMessage(userText: String) {
        // Перевірка на дурня: якщо текст пустий, нічого не робимо.
        if (userText.isBlank()) return

        // 1. Миттєво показуємо повідомлення користувача на екрані
        addMessage(ChatMessage(userText, isUser = true))

        // 2. Вмикаємо індикатор завантаження
        _isLoading.value = true

        // 3. Запускаємо Корутину (viewModelScope.launch):
        // Запит до AI — це робота з мережею, вона триває 1-3 секунди.
        // Ми робимо це у фоновому потоці, щоб телефон не "завис".
        viewModelScope.launch {
            try {
                // 4. ПРОМПТ-ІНЖИНІРИНГ (Prompt Engineering):
                // Ми не просто кидаємо текст користувача в AI.
                // Ми додаємо інструкцію (System Instruction), щоб AI поводився як кухар,
                // а не як звичайний чат-бот.
                val prompt = "Ти професійний шеф-кухар. У користувача є: \"$userText\". " +
                        "Придумай один смачний рецепт українською мовою. " +
                        "Напиши список інгредієнтів і покрокову інструкцію."

                // 5. Відправляємо запит і чекаємо відповідь (generateContent)
                val response = generativeModel.generateContent(prompt)

                // Перевіряємо, чи прийшов текст. Якщо ні — ставимо заглушку.
                val responseText = response.text ?: "Не вдалося отримати відповідь."

                // 6. Додаємо відповідь AI у чат
                addMessage(ChatMessage(responseText, isUser = false))

            } catch (e: Exception) {
                // 7. Обробка помилок:
                // Якщо зник інтернет або Firebase впав, ми не крашимо додаток,
                // а показуємо червоне повідомлення про помилку.
                val errorMsg = "Помилка: ${e.localizedMessage}"
                addMessage(ChatMessage(errorMsg, isUser = false, isError = true))
            } finally {
                // 8. Вимикаємо спіннер (незалежно від того, був успіх чи помилка)
                _isLoading.value = false
            }
        }
    }

    // Допоміжна функція для додавання повідомлення в список
    private fun addMessage(msg: ChatMessage) {
        // StateFlow вимагає створення НОВОГО списку при кожній зміні.
        // Ми беремо старий список + нове повідомлення.
        _messages.value = _messages.value + msg
    }
}