// [ВАЖЛИВО] Пакет тепер вказує на 3 лабу
package com.example.a3laba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// [ВАЖЛИВО] Імпорт теми для 3 лаби (переконайся, що перейменував її в файлі Theme.kt)
import com.example.a3laba.ui.theme._3labaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Використовуємо тему 3-ї лаби
            _3labaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // [ВИПРАВЛЕНО] Тепер передаємо innerPadding правильно (без "paddingValues =")
                    TodoApp(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun TodoApp(modifier: Modifier = Modifier) {
    // 1. Змінна для тексту (Стан)
    var currentTaskText by remember { mutableStateOf("") }

    // 2. Змінна для списку (Стан)
    var tasksList by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Введіть нове завдання (Лаба 3):", // Трохи змінив текст, щоб видно було різницю
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = currentTaskText,
                onValueChange = { newText ->
                    currentTaskText = newText
                },
                label = { Text("Завдання...") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (currentTaskText.isNotBlank()) {
                        tasksList = tasksList + currentTaskText
                        currentTaskText = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Додати")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Список справ:",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tasksList) { task ->
                Text(
                    text = "• $task",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    _3labaTheme {
        TodoApp()
    }
}