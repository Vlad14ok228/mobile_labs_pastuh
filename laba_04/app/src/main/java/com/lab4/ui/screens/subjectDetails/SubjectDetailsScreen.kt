package com.lab4.ui.screens.subjectDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab4.data.db.DatabaseStorage
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity
import com.lab4.ui.navigation.SubjectDetailsRoute
import kotlinx.coroutines.launch

@Composable
fun SubjectDetailsScreen(route: SubjectDetailsRoute) {
    // 1. Отримання залежностей
    val context = LocalContext.current
    // Отримуємо екземпляр бази даних (Singleton), щоб робити запити
    val db = DatabaseStorage.getDatabase(context)

    // Створюємо CoroutineScope. Це необхідно, бо операції з БД (Insert, Update)
    // є асинхронними (suspend), і їх не можна викликати напряму з UI-потоку (наприклад, з onClick).
    val scope = rememberCoroutineScope()

    // 2. Стан UI (State)
    // Зберігаємо дані про предмет та список його лаб у стані.
    // 'remember' гарантує, що дані не зникнуть при перемальовуванні екрана.
    // 'mutableStateOf' дозволяє інтерфейсу автоматично оновлюватися, коли дані зміняться.
    var subject by remember { mutableStateOf<SubjectEntity?>(null) }
    var labs by remember { mutableStateOf<List<SubjectLabEntity>>(emptyList()) }

    // 3. Завантаження даних (Side Effect)
    // LaunchedEffect(Unit) запускається лише ОДИН РАЗ при вході на екран.
    // Тут ми робимо запит до БД, щоб отримати назву предмета та список лаб за ID.
    LaunchedEffect(Unit) {
        subject = db.subjectsDao.getSubjectById(route.id)
        labs = db.subjectLabsDao.getSubjectLabsBySubjectId(route.id)
    }

    Column(Modifier.padding(16.dp)) {
        // Відображаємо назву предмета (якщо завантажилась)
        Text(text = subject?.title ?: "", fontSize = 28.sp)
        Spacer(Modifier.height(16.dp))
        Text("Лабораторні роботи:", fontSize = 22.sp)

        // 4. Список (LazyColumn)
        // Використовуємо LazyColumn для ефективності: він малює тільки ті елементи,
        // які зараз видно на екрані.
        LazyColumn {
            items(labs) { lab ->
                // Локальний стан для кожного елемента списку (чи відкрите меню, текст коментаря)
                var expanded by remember { mutableStateOf(false) }
                var comment by remember { mutableStateOf(lab.comment ?: "") }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(text = lab.title, fontSize = 20.sp)
                        Text(text = lab.description, fontSize = 16.sp)

                        Spacer(Modifier.height(8.dp))

                        // Відображення поточного статусу з БД
                        Text("Статус: ${lab.status}")

                        // Кнопка відкриття меню зміни статусу
                        Button(onClick = { expanded = !expanded }) {
                            Text("Обрати статус")
                        }

                        // 5. Логіка оновлення статусу
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("Не розпочато", "В процесі", "Відкласти", "Готово").forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status) },
                                    onClick = {
                                        expanded = false
                                        // Запускаємо корутину для запису в БД
                                        scope.launch {
                                            // Апдейтимо статус в базі
                                            db.subjectLabsDao.updateStatus(lab.id!!, status)
                                            // ВАЖЛИВО: Одразу зчитуємо оновлений список з бази,
                                            // щоб UI оновився і показав новий статус
                                            labs = db.subjectLabsDao.getSubjectLabsBySubjectId(route.id)
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        Text("Коментар:")
                        // Поле для введення коментаря
                        BasicTextField(
                            value = comment,
                            onValueChange = { comment = it }, // Оновлюємо локальну змінну при введенні
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        )

                        // 6. Логіка збереження коментаря
                        Button(
                            onClick = {
                                scope.launch {
                                    // Виконуємо UPDATE запит для коментаря
                                    db.subjectLabsDao.updateComment(lab.id!!, comment)
                                    // Оновлюємо UI актуальними даними
                                    labs = db.subjectLabsDao.getSubjectLabsBySubjectId(route.id)
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Зберегти коментар")
                        }
                    }
                }
            }
        }
    }
}