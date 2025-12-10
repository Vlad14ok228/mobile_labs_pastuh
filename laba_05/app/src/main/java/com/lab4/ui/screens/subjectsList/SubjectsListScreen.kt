package com.lab4.ui.screens.subjectsList

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubjectsListScreen(
    viewModel: SubjectsListViewModel = koinViewModel(), // 👈 ViewModel: Отримуємо об'єкт через Koin для доступу до даних
    onDetailsScreen: (Int) -> Unit, // 👈 Лямбда-функція: Це КОМАНДА для навігації. Вона викликається при кліку.
) {
    // --- ПІДПИСКА НА ДАНІ ---
    // Ми "слухаємо" потік даних (Flow) із ViewModel.
    // Коли дані предметів завантажуються з бази, collectAsState() оновлює 'subjectsListState'.
    val subjectsListState = viewModel.subjectListStateFlow.collectAsState()

    // --- МАКЕТ (LAYOUT) ---
    LazyColumn(Modifier.fillMaxSize()) { // 👈 LazyColumn: Оптимізований список, який малює лише видимі елементи

        // items: Цикл, який створює елемент для кожного предмета у списку
        items(subjectsListState.value) { subject ->
            Text(
                text = subject.title, // 👈 Відображаємо назву предмета
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    // --- ЛОГІКА КЛІКУ ---
                    .clickable(
                        interactionSource = null,
                        indication = LocalIndication.current,
                    ) {
                        // Коли користувач натискає:
                        subject.id?.let { id ->
                            // 1. Перевіряємо, чи є у предмета ID.
                            // 2. Якщо ID є, викликаємо функцію навігації (onDetailsScreen)
                            // 3. Передаємо ID, щоб NavGraph міг перейти на екран деталей і знати, який предмет відкрити.
                            onDetailsScreen(id)
                        }
                    }
            )
        }
    }
}