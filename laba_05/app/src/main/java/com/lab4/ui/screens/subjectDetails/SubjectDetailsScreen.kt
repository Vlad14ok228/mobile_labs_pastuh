package com.lab4.ui.screens.subjectDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab4.ui.navigation.SubjectDetailsRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubjectDetailsScreen(
    route: SubjectDetailsRoute, // Отримуємо аргумент навігації (містить ID предмета)
    // [ВАЖЛИВО] Koin автоматично створює і надає нам потрібну ViewModel
    viewModel: SubjectDetailsViewModel = koinViewModel()
) {
    // --- ПІДПИСКА НА ДАНІ (OBSERVING) ---
    // Ми "слухаємо" потоки даних (Flow) з ViewModel.
    // collectAsState перетворює їх на State: як тільки база даних оновиться,
    // ці змінні зміняться, і екран перемалюється автоматично.
    val subjectState = viewModel.subjectStateFlow.collectAsState()
    val labsState = viewModel.subjectLabsListStateFlow.collectAsState()

    // [ВАЖЛИВО] ЗАВАНТАЖЕННЯ ДАНИХ
    // LaunchedEffect запускається один раз при вході на екран.
    // Ми кажемо ViewModel: "Завантаж дані для предмета з цим ID".
    LaunchedEffect(Unit) { viewModel.initData(route.id) }

    Column(Modifier.padding(16.dp)) {
        // Відображаємо назву предмета (якщо вона вже завантажилась)
        Text(subjectState.value?.title ?: "", fontSize = 28.sp)

        Spacer(Modifier.height(16.dp))
        Text("Лабораторні роботи:", fontSize = 22.sp)

        // Список лабораторних робіт
        LazyColumn {
            items(labsState.value) { lab -> // Проходимо по списку лаб, які прийшли з БД

                // Локальний стан для інтерфейсу (чи відкрите меню, що написано в коментарі)
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
                        Text("Статус: ${lab.status ?: "Невідомо"}")

                        // --- [ФУНКЦІОНАЛ] ЗМІНА СТАТУСУ ---
                        Button(onClick = { expanded = !expanded }) {
                            Text("Змінити статус")
                        }

                        // Випадаюче меню зі варіантами статусів
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("Не розпочато", "В процесі", "Відкладено", "Виконано").forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status) },
                                    onClick = {
                                        expanded = false
                                        // [ВАЖЛИВО] Викликаємо метод ViewModel для оновлення статусу в БД
                                        viewModel.updateStatus(lab.id!!, status)
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // --- [ФУНКЦІОНАЛ] КОМЕНТАРІ ---
                        Text("Коментар:")
                        BasicTextField(
                            value = comment,
                            onValueChange = { comment = it }, // Оновлюємо локальну змінну, поки юзер друкує
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        )
                        Button(
                            // [ВАЖЛИВО] При кліку зберігаємо коментар у Базу Даних через ViewModel
                            onClick = { viewModel.updateComment(lab.id!!, comment) },
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
