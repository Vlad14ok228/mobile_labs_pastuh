// 1. ПАКЕТ: Папка ui/screens
package com.example.smartrecipeapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smartrecipeapp.ui.components.MealItem
import com.example.smartrecipeapp.ui.screens.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    // Навігація: коли клікаємо на картку, треба відкрити деталі.
    // Екран сам не вміє переходити, він просить про це батьківський NavHost.
    onMealClick: (String) -> Unit
) {
    // 2. ОТРИМАННЯ VIEWMODEL:
    // Ми використовуємо HomeViewModel, бо там вже написана логіка роботи з БД (favoriteMeals).
    // Немає сенсу створювати окремий FavoritesViewModel, якщо код дублюється.
    val viewModel: HomeViewModel = koinViewModel()

    // 3. ПІДПИСКА НА БАЗУ ДАНИХ:
    // viewModel.favoriteMeals — це Flow (потік), який йде прямо з Room.
    // collectAsState перетворює його на звичайний список.
    // initial = emptyList(): Поки база даних "прокидається" (мілісекунди), ми показуємо пустий список, щоб не було помилки.
    val favorites by viewModel.favoriteMeals.collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {

        // 4. ПЕРЕВІРКА НА ПУСТОТУ (Empty State):
        // Це важливий елемент UX. Не можна показувати просто білий екран.
        // Якщо збережених рецептів 0 — кажемо про це користувачу.
        if (favorites.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center), // Центруємо по екрану
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "У вас поки немає збережених рецептів.\nПоставте ❤️ на головному екрані!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // 5. ЯКЩО Є РЕЦЕПТИ — МАЛЮЄМО СПИСОК:
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // items(favorites): Беремо список, який прийшов з БД
                items(favorites) { meal ->

                    // Обгортаємо в Box, щоб зробити всю картку клікабельною для переходу до деталей
                    Box(modifier = Modifier.clickable { onMealClick(meal.id) }) {
                        MealItem(
                            meal = meal,

                            // isFavorite = true: Оскільки ми на екрані "Улюблені",
                            // логічно, що всі страви тут — улюблені.
                            // Тому ми "жорстко" передаємо true, щоб сердечко було червоним.
                            isFavorite = true,

                            // onToggleFavorite: Якщо користувач натисне сердечко ТУТ,
                            // ми викликаємо функцію видалення.
                            // Flow автоматично оновить список, і страва зникне з екрану на очах.
                            onToggleFavorite = { viewModel.toggleFavorite(meal) }
                        )
                    }
                }
            }
        }
    }
}