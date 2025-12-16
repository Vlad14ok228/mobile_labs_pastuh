// 1. ПАКЕТ: Екран лежить у папці ui/screens/details
package com.example.smartrecipeapp.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    // ID страви, яку треба завантажити (приходить з навігації)
    mealId: String,
    // ViewModel отримуємо автоматично через Koin
    viewModel: DetailsViewModel = koinViewModel()
) {
    // 2. LAUNCHED EFFECT (Побічний ефект):
    // Цей блок коду спрацює ТІЛЬКИ ОДИН РАЗ при відкритті екрану (або якщо зміниться mealId).
    // Ми кажемо ViewModel: "Ей, завантаж мені дані про страву з цим ID!".
    LaunchedEffect(mealId) {
        viewModel.loadMeal(mealId)
    }

    // 3. ПІДПИСКА НА СТАН (UI State):
    // Ми слідкуємо за змінною uiState у ViewModel.
    // Якщо там зміниться статус (Loading -> Success), екран перемалюється.
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        // 4. ОБРОБКА СТАНІВ (Pattern Matching):
        // Перевіряємо, що зараз відбувається з даними.
        when (val currentState = state) {

            // СТАН 1: Завантаження
            is DetailsUiState.Loading -> {
                // Показуємо крутилку по центру екрану
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // СТАН 2: Помилка (наприклад, немає інтернету)
            is DetailsUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // СТАН 3: Успіх (Дані отримано!)
            is DetailsUiState.Success -> {
                val meal = currentState.meal

                // 5. ОКРЕМА ПІДПИСКА НА "УЛЮБЛЕНЕ":
                // Ми підписуємося на Flow з бази даних конкретно для цієї страви.
                // initial = false означає, що поки ми не перевірили базу, вважаємо, що лайка немає.
                val isFavorite by viewModel.isMealFavorite(meal.id).collectAsState(initial = false)

                // Вміст екрану з прокруткою (бо рецепт може бути довгим)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Додає скрол
                ) {
                    // 6. ВЕЛИКА КАРТИНКА + КНОПКА:
                    // Використовуємо Box, щоб накласти кнопку поверх картинки.
                    Box {
                        AsyncImage(
                            model = meal.imageUrl, // URL з інтернету
                            contentDescription = meal.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp), // Висота шапки
                            contentScale = ContentScale.Crop // Обрізаємо фото красиво
                        )

                        // FloatingActionButton (Кругла кнопка лайка)
                        FloatingActionButton(
                            onClick = { viewModel.toggleFavorite(meal) }, // Клік -> у ViewModel
                            modifier = Modifier
                                .align(Alignment.BottomEnd) // Притискаємо вправо вниз
                                .padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            // Змінюємо іконку та колір залежно від isFavorite
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }

                    // 7. ТЕКСТОВИЙ ОПИС:
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = meal.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // Category • Area (напр. "Dessert • French")
                        Text(
                            text = "${meal.category} • ${meal.area}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Інструкція приготування:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Сама інструкція. ?: "..." - це захист від null.
                        Text(
                            text = meal.instructions ?: "Інструкції немає",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}