package com.lab4.ui.screens.subjectsList

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab4.data.db.DatabaseStorage
import com.lab4.data.entity.SubjectEntity

@Composable
fun SubjectsListScreen(
    // Це функція зворотного виклику (callback).
    // Ми викликаємо її, коли користувач натискає на предмет, щоб NavGraph знав, що треба переключити екран.
    onDetailsScreen: (Int) -> Unit
) {
    // 1. Налаштування доступу до Бази Даних
    val context = LocalContext.current // Отримуємо контекст Android (потрібен для доступу до системних ресурсів)

    // Отримуємо екземпляр нашої бази даних (використовуючи Singleton, який ми створили в DatabaseStorage).
    val db = DatabaseStorage.getDatabase(context)

    // 2. Стан (State) для збереження даних
    // 'subjects' - це змінна, яка зберігає список предметів, завантажених з БД.
    // 'remember' - не дає змінній зникнути при перемальовуванні екрана.
    // 'mutableStateOf' - робить змінну "живою": як тільки ми запишемо сюди дані, екран оновиться сам.
    var subjects by remember { mutableStateOf<List<SubjectEntity>>(emptyList()) }

    // 3. Завантаження даних (Асинхронно)
    // LaunchedEffect(Unit) - це блок коду, який запускається лише ОДИН РАЗ при відкритті екрана.
    LaunchedEffect(Unit) {
        // Ми звертаємося до DAO (subjectsDao) і виконуємо SQL-запит "SELECT * FROM subjects".
        // Отриманий список записуємо в нашу змінну стану.
        subjects = db.subjectsDao.getAllSubjects()
    }

    // 4. Відображення списку (UI)
    // LazyColumn - це оптимізований список (аналог RecyclerView).
    // Він створює елементи тільки тоді, коли вони з'являються на екрані.
    LazyColumn(Modifier.fillMaxSize()) {

        // items() - це цикл, який проходить по кожному елементу зі списку 'subjects'.
        items(subjects) { subject ->

            // Для кожного предмета створюємо текстове поле
            Text(
                text = subject.title, // Беремо назву предмета з об'єкта Entity
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth() // Розтягуємо текст на всю ширину екрана
                    .padding(16.dp) // Додаємо відступи для краси
                    // --- ЛОГІКА КЛІКУ ---
                    .clickable(
                        interactionSource = null,
                        indication = LocalIndication.current, // Додає ефект натискання ("хвильку")
                    ) {
                        // Цей код виконується при натисканні на предмет:

                        // Перевіряємо, чи ID не null (безпека Kotlin)
                        subject.id?.let { id ->
                            // Викликаємо функцію навігації і передаємо їй ID цього предмета.
                            // Це сигнал для NavigationGraph: "Відкрий деталі для предмета №(id)"
                            onDetailsScreen(id)
                        }
                    }
            )
        }
    }
}