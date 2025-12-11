package com.vovan.lab7.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vovan.lab7.data.entity.TextPair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Клас-Репозиторій: ізолює логіку спілкування зі сторонніми службами (тут — з ШІ).
class GeminiAIRepository {
    companion object{
        private const val MODEL = "gemini-2.5-flash"
        // 👈 Константа: Визначення назви моделі ШІ, яку ми використовуємо. 'gemini-2.5-flash' — швидка модель для таких завдань.

        // --- Приклади Промптів ---

        private val PROMPT_TEXT_PAIR_LIST = """
            Generate 5 random text1 to text2 pairs.
            Return ONLY a valid JSON array in this format:
            [
              {"text1": "string", "text2": "string"},
              ...
            ]
        """.trimIndent()
        // 👈 Приклад промпту для тестування: просить модель повернути простий JSON-масив (формат, який ми очікуємо).

        private val PROMPT_TRIVIA_HISTORY = """
            Generate 5 trivia questions with short answers related to world history.
            Return ONLY a valid JSON array in this format:
            [
              {"question": "string", "answer": "string"},
              ...
            ]
        """.trimIndent()
        // 👈 Приклад промпту для конкретної теми: просить згенерувати питання про Історію світу у потрібному JSON-форматі.
    }

    // --- Ініціалізація ---

    // Ініціалізація aiModel: Створюємо робочий екземпляр моделі Gemini, вказуючи, яку модель використовувати.
    private val aiModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(MODEL)

    // Ініціалізація gson parser: Об'єкт, який буде перетворювати JSON-текст на наш data class (TextPair).
    private val gson = Gson()

    /**
     * generateQuizByTopic(topic: String): List<TextPair>? - Функція, яка виконує запит
     * до ШІ для генерації вікторини за вказаною темою.
     * */
    suspend fun generateQuizByTopic(topic: String): List<TextPair>? {
        return try {
            withContext(Dispatchers.IO) {
                // --- Формування Промпту ---
                val prompt = """
                Generate 5 quiz questions with short answers about "$topic".
                Return ONLY a valid JSON array in this format:
                [
                  {"question": "string", "answer": "string"},
                  ...
                ]
            """.trimIndent()
                // ☝️ Це динамічний промпт: він вставляє тему, яку ввів користувач (наприклад, "Космос"), у запит до ШІ.

                // 1. Виконання запиту до ШІ
                val response = aiModel.generateContent(prompt)

                // 2. Очищення відповіді
                val outputRaw = response.text ?: ""
                // ШІ іноді додає маркування ```json і ```. Ми видаляємо їх, щоб отримати чистий JSON.
                val outputJson = outputRaw.replace(Regex("```json|```"), "").trim()

                // 3. Парсинг JSON
                // Встановлюємо, що ми очікуємо отримати список об'єктів TextPair.
                val type = object : TypeToken<List<TextPair>>() {}.type
                // Використовуємо Gson для перетворення чистого JSON-тексту у List<TextPair>.
                gson.fromJson<List<TextPair>>(outputJson, type)
            }
        } catch (e: Exception) {
            // Обробка помилок (наприклад, проблеми з мережею або невірний JSON-формат).
            Log.e("GeminiAIRepository", "Error generating quiz: $e")
            null
        }
    }
}