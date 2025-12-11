package com.vovan.lab7.ui.screens.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntryScreen(onStartGame: (String) -> Unit) {
    // 1. Аргумент (Callback)
    // 👈 Лямбда-функція: Функція, яка викликається, коли користувач готовий почати гру.
    // Вона приймає як параметр обрану або введену користувачем тему (String).

    // --- 2. Дані та Стан ---
    val topics = listOf("History", "Science", "Sport") // Фіксований список рекомендованих тем.
    var customTopic by remember { mutableStateOf("") } // 👈 Стан: Зберігає текст, який користувач вводить у полі.
    // 'remember' і 'mutableStateOf' гарантують, що введений текст збережеться і оновиться на екрані.

    // --- 3. UI: Загальний Макет ---
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        // Scaffold: Надає стандартну структуру (якщо б ми використовували TopBar, Snackbar тощо).

        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Центрує елементи по горизонталі.
            verticalArrangement = Arrangement.Center, // Центрує елементи по вертикалі.
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Text("Choose a quiz topic:")

            // --- 4. Список Карток (Фіксовані Теми) ---
            topics.forEach { topic -> // Проходимо по фіксованому списку тем.
                Card(
                    onClick = { onStartGame(topic) }, // 👈 При кліку викликаємо onStartGame, передаючи фіксовану тему.
                    modifier = Modifier.padding(8.dp).height(60.dp)
                ) {
                    // Box використовується для центрованого розташування тексту всередині картки.
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(topic) // Відображаємо назву фіксованої теми.
                    }
                }
            }

            // --- 5. Поле для Введення Власної Теми ---
            Text("Or enter your own topic:")
            TextField(
                value = customTopic, // Значення береться зі стану customTopic.
                onValueChange = { customTopic = it }, // При введенні тексту оновлюємо стан customTopic.
                placeholder = { Text("Enter topic...") }
            )

            // 6. Кнопка "Почати"
            Button(
                onClick = {
                    // Перевіряємо, чи в полі щось введено, перш ніж починати гру.
                    if (customTopic.isNotBlank()) onStartGame(customTopic)
                }
            ) {
                Text("Start Quiz")
            }
        }
    }
}
