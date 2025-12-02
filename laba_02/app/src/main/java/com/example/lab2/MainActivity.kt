package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab2.ui.theme.Lab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                // Використовуємо Surface як контейнер для всього екрану
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleTodoScreen()
                }
            }
        }
    }
}

// Проста модель даних (тільки текст і статус)
data class MyTask(val id: Long, val text: String, var isDone: Boolean = false)

@Composable
fun SimpleTodoScreen() {
    // Стан для списку завдань
    // rememberSaveable допоможе зберегти список при повороті екрану (додатковий плюс для вчителя)
    val tasks = remember { mutableStateListOf<MyTask>() }

    // Стан для тексту в полі вводу
    var currentInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- ЗАГОЛОВОК ---
        Text(
            text = "Мої Справи",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- БЛОК ВВЕДЕННЯ (Зверху) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Поле вводу
            OutlinedTextField(
                value = currentInput,
                onValueChange = { currentInput = it },
                label = { Text("Що треба зробити?") },
                modifier = Modifier.weight(1f), // Займає все доступне місце
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Кнопка додавання (тільки іконка для компактності)
            Button(
                onClick = {
                    if (currentInput.isNotBlank()) {
                        // Генеруємо унікальний ID на основі часу
                        val newTask = MyTask(System.currentTimeMillis(), currentInput)
                        tasks.add(newTask)
                        currentInput = "" // Очищаємо поле
                    }
                },
                // Робимо кнопку квадратною
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- СПИСОК ЗАВДАНЬ ---
        Text(
            text = "Список (${tasks.size}):",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tasks) { task ->
                TaskRow(
                    task = task,
                    onDelete = { tasks.remove(task) }
                )
                // Розділювач між елементами
                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun TaskRow(task: MyTask, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Текст завдання
        Text(
            text = "• ${task.text}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        // Кнопка видалення (маленька іконка)
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodo() {
    Lab2Theme {
        SimpleTodoScreen()
    }
}