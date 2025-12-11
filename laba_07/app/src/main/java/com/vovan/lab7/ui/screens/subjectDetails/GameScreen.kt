package com.vovan.lab7.ui.screens.subjectDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(
    topic: String, // 👈 Аргумент 1: Назва теми, отримана з навігації (NavigationGraph).
    viewModel: GameScreenViewModel = koinViewModel(), // 👈 Аргумент 2: Отримуємо ViewModel через Koin для логіки та стану.
) {
    // 1. Підписка на Стан (Observing)
    val isLoading = viewModel.isLoading.collectAsState() // 👈 Стан завантаження: true, коли чекаємо відповіді від ШІ.
    val quizList = viewModel.quizList.collectAsState() // 👈 Стан списку: Зберігає список згенерованих питань/відповідей (List<TextPair>).

    // 2. Ініціація Завантаження (Side Effect)
    LaunchedEffect(topic) {
        // 👈 LaunchedEffect: Блок виконується, коли компонент з'являється.
        // (topic): Вказує, що якщо тема зміниться (хоча в цій лабі не змінюється), код виконається знову.
        viewModel.loadQuiz(topic)
        // ☝️ Викликаємо головну функцію ViewModel, яка ініціює запит до Gemini AI з переданою темою.
    }

    // --- 3. UI: Макет ---
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        // Box використовується для центрування елементів на екрані.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center // Центруємо вміст (індикатор завантаження або список).
        ) {
            // 4. Обробка Стану Екрана
            when {
                isLoading.value -> CircularProgressIndicator() // 👈 Якщо isLoading = true, показуємо індикатор завантаження.

                quizList.value != null -> {
                    // 👈 Якщо список питань завантажено (не null), показуємо список.
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(quizList.value!!) { item -> // Цикл по кожному об'єкту TextPair (Питання/Відповідь).

                            // 5. Локальний Стан для Відповіді
                            var showAnswer by remember { mutableStateOf(false) }
                            // 👈 Стан: Визначає, чи розгорнута картка (показана відповідь, чи ні).

                            Card(onClick = { showAnswer = !showAnswer }) {
                                // 👈 При натисканні на картку перемикаємо стан showAnswer.
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(item.question, fontSize = 18.sp, fontWeight = FontWeight.Bold) // 👈 Завжди показуємо питання.

                                    AnimatedVisibility(showAnswer) {
                                        // 👈 AnimatedVisibility: Компонент, який показує/приховує вміст з анімацією.
                                        // Він показує відповідь лише, якщо showAnswer = true.
                                        Text(item.answer, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                else -> Text("No quiz generated.") // 👈 Якщо isLoading = false, і quizList = null (помилка), показуємо повідомлення.
            }
        }
    }
}