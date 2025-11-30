// --- Пакет вашого нового проєкту ---
package com.example.a2laba

// --- Всі імпорти ---
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
import com.example.a2laba.ui.theme._2labaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [ВАЖЛИВО] Вмикає режим на повний екран
        // Контент буде "залазити" під годинник і нижню смужку навігації.
        enableEdgeToEdge()

        setContent {
            _2labaTheme {
                // [ВАЖЛИВО] Scaffold (Риштування)
                // Це стандартний макет екрана. Він дає нам 'innerPadding'.
                // 'innerPadding' - це точний розмір статус-бару, щоб ми не перекрили годинник текстом.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Викликаємо нашу функцію і передаємо їй ці безпечні відступи
                    TodoApp(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

/**
 * Головна функція. 'modifier' передається згори, щоб врахувати відступи від країв екрана.
 */
@Composable
fun TodoApp(modifier: Modifier = Modifier) {

    // --- [НОВЕ] РОБОТА ЗІ СТАНОМ (STATE) ---

    // 1. Змінна для тексту.
    // 'remember' - запам'ятовує значення, щоб воно не зникло, коли екран оновиться.
    // 'mutableStateOf' - каже екрану: "Якщо я змінюсь, перемалюй поле вводу".
    var currentTaskText by remember { mutableStateOf("") }

    // 2. Змінна для списку.
    // Зберігає всі наші завдання. Спочатку список пустий.
    var tasksList by remember { mutableStateOf(listOf<String>()) }

    // --- [НОВЕ] ВЕРТИКАЛЬНИЙ МАКЕТ ---
    // Column розставляє елементи один під одним (Зверху -> Вниз)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Додатковий відступ 16dp з усіх боків для краси
    ) {

        // Просто заголовок
        Text(
            text = "Введіть нове завдання:",
            style = MaterialTheme.typography.titleMedium
        )

        // --- [НОВЕ] ГОРИЗОНТАЛЬНИЙ МАКЕТ ---
        // Row розставляє елементи в рядок (Зліва -> Направо)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Розтягує елементи по краях
        ) {

            // --- [НОВЕ] ПОЛЕ ВВОДУ ---
            OutlinedTextField(
                value = currentTaskText, // [ВАЖЛИВО] Прив'язка: поле показує те, що в змінній
                onValueChange = { newText ->
                    // [ВАЖЛИВО] Оновлення: коли тиснеш клавішу, змінна оновлюється
                    currentTaskText = newText
                },
                label = { Text("Наприклад, 'купити молоко'") },
                // weight(1f) змушує поле зайняти всю доступну ширину, залишивши місце лише для кнопки
                modifier = Modifier.weight(1f)
            )

            // --- [НОВЕ] КНОПКА ---
            Button(
                onClick = {
                    // Код, що виконується при натисканні:
                    if (currentTaskText.isNotBlank()) { // 1. Перевірка: чи не пустий текст

                        // 2. [ВАЖЛИВО] Додавання в список
                        // Ми беремо старий список і додаємо до нього новий елемент.
                        // Це змушує LazyColumn побачити зміни і перемалюватися.
                        tasksList = tasksList + currentTaskText

                        // 3. Очищення поля вводу
                        currentTaskText = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp) // Відступ зліва від кнопки
            ) {
                Text("Додати")
            }
        }

        // Просто пустий простір (відступ) висотою 24dp
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Список ваших справ:",
            style = MaterialTheme.typography.titleMedium
        )

        // --- [НОВЕ] РОЗУМНИЙ СПИСОК (LazyColumn) ---
        // Використовуємо його замість Column, бо він економить пам'ять.
        // Він малює тільки ті елементи, які зараз видно на екрані телефону.
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            // items() - це як цикл 'for'. Він біжить по кожному елементу списку.
            items(tasksList) { task ->
                // Для кожного завдання створюється текст
                Text(
                    text = "• $task",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}


// Функція для попереднього перегляду в Android Studio (щоб не запускати емулятор)
@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    _2labaTheme {
        TodoApp()
    }
}