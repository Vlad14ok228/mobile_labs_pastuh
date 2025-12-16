// 1. ПАКЕТ: Папка ui/screens/home
package com.example.smartrecipeapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartrecipeapp.ui.components.MealItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    // Callback навігації.
    // Екран сам не знає, як переходити на деталі. Він просто "кричить" батьківському компоненту:
    // "Ей, клікнули на страву з ID 123!". А вже MainActivity вирішує, куди переходити.
    onMealClick: (String) -> Unit
) {
    // 2. ОТРИМАННЯ VIEWMODEL:
    // Використовуємо Koin, щоб отримати готовий екземпляр HomeViewModel.
    val viewModel: HomeViewModel = koinViewModel()

    // 3. ПІДПИСКА НА СТАН:
    // Слідкуємо за uiState (Завантаження / Успіх / Помилка).
    // Тільки-но дані зміняться, екран перемалюється.
    val state by viewModel.uiState.collectAsState()

    // 4. ЛОКАЛЬНИЙ СТАН:
    // Текст, який юзер вводить у пошук. Зберігаємо його тут, бо це UI-дрібниця.
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- ПОЛЕ ПОШУКУ (Зафіксоване зверху) ---
        // Воно не всередині LazyColumn, тому воно ЗАВЖДИ буде видно, навіть коли скролиш список.
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it }, // Оновлюємо змінну при введенні літер
            label = { Text("Пошук (напр. Chicken, Pie)") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                // Кнопка пошуку (Лупа)
                IconButton(onClick = { viewModel.searchMeal(searchText) }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            singleLine = true // Текст в один рядок
        )

        Spacer(modifier = Modifier.height(16.dp)) // Відступ

        // --- ВМІСТ ЕКРАНУ ---
        // Використовуємо Box, щоб центрувати помилки або спіннер завантаження.
        Box(modifier = Modifier.fillMaxSize()) {

            // Pattern Matching: перевіряємо, в якому стані зараз екран
            when (val currentState = state) {

                // СТАН: ЗАВАНТАЖЕННЯ (Крутилка)
                is HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                // СТАН: УСПІХ (Показуємо список)
                is HomeUiState.Success -> {
                    // LazyColumn = Розумний список (як RecyclerView).
                    LazyColumn {

                        // item { ... } - додає ОДИН елемент у список (Заголовок).
                        // Цей текст буде скролитися разом з рецептами.
                        item {
                            Text(
                                // Динамічний текст: якщо поле пошуку пусте -> "Страва Дня", інакше "Результати"
                                text = if (searchText.isEmpty()) "🔥 Страва Дня" else "🔎 Результати пошуку",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        // items(...) - додає БАГАТО елементів (сам список рецептів).
                        items(currentState.meals) { meal ->

                            // Для кожної картки окремо перевіряємо, чи вона лайкнута.
                            // initial = false (поки база думає, сердечко пусте).
                            val isFavorite by viewModel.isMealFavorite(meal.id).collectAsState(initial = false)

                            // Обгортаємо в Box, щоб додати клікабельність на всю картку
                            Box(modifier = Modifier.clickable { onMealClick(meal.id) }) {
                                MealItem(
                                    meal = meal,
                                    isFavorite = isFavorite,
                                    // Передаємо функцію: що робити при кліку на серце
                                    onToggleFavorite = { viewModel.toggleFavorite(meal) }
                                )
                            }
                        }
                    }
                }

                // СТАН: ПОМИЛКА
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = currentState.message, color = Color.Red)
                            Spacer(modifier = Modifier.height(8.dp))
                            // Кнопка "Спробувати ще раз"
                            Button(onClick = { viewModel.loadRandomMeal() }) {
                                Text("Спробувати ще раз")
                            }
                        }
                    }
                }
            }
        }
    }
}