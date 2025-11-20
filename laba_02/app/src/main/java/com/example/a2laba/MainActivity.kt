// --- Пакет вашого нового проєкту ---
package com.example.a2laba

// --- Всі імпорти, які потрібні для ToDo + Scaffold ---
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

// --- Імпорт вашої нової теми ---
import com.example.a2laba.ui.theme._2labaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Включаємо режим "від краю до краю"
        setContent {
            _2labaTheme {
                // Використовуємо Scaffold, як у вашому прикладі
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // --- ОСЬ ТУТ МИ ВСТАВЛЯЄМО ВАШУ ЛАБУ ---
                    // Ми викликаємо TodoApp і передаємо йому
                    // відступи (innerPadding) від Scaffold
                    TodoApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                    // ----------------------------------------

                }
            }
        }
    }
}

/**
 * Головна Composable-функція, що містить всю логіку ToDo-додатку.
 * Тепер вона приймає 'modifier', щоб ми могли передати відступи.
 */
@Composable
fun TodoApp(modifier: Modifier = Modifier) {
    // 1. Змінна, що зберігає текст, який користувач вводить у поле
    var currentTaskText by remember { mutableStateOf("") }

    // 2. Список, що зберігає всі додані завдання
    var tasksList by remember { mutableStateOf(listOf<String>()) }

    // Використовуємо Column, щоб розташувати елементи вертикально
    // Додаємо наш 'modifier' (який містить innerPadding) до Column
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Додаємо ще свої відступи з усіх боків
    ) {

        // --- Секція 1: Введення завдання ---
        Text(
            text = "Введіть нове завдання:",
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
                label = { Text("Наприклад, 'купити молоко'") },
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

        // --- Секція 2: Список завдань ---
        Text(
            text = "Список ваших справ:",
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


// Функція для попереднього перегляду (використовує _2labaTheme)
@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    _2labaTheme {
        TodoApp()
    }
}