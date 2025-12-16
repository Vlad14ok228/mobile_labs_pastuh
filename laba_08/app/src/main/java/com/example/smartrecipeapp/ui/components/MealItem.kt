// 1. ПАКЕТ: Папка ui/components (для багаторазових елементів інтерфейсу)
package com.example.smartrecipeapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.smartrecipeapp.data.model.Meal

// 2. @Composable: Це функція, яка малює частину інтерфейсу.
@Composable
fun MealItem(
    // Дані про страву (назва, картинка, опис)
    meal: Meal,

    // Стан: чи є ця страва в улюблених (true/false).
    // Ми НЕ перевіряємо базу даних тут. Ми просто отримуємо готове значення true або false.
    isFavorite: Boolean,

    // Callback (Зворотний виклик): Функція, яка запуститься, коли юзер натисне на сердечко.
    // () -> Unit означає "функція, яка нічого не приймає і нічого не повертає".
    onToggleFavorite: () -> Unit
) {
    // 3. Card: Картка з тінню (elevation) та заокругленими кутами.
    Card(
        modifier = Modifier
            .fillMaxWidth() // Розтягуємо на всю ширину екрану
            .padding(vertical = 8.dp), // Відступи зверху і знизу між картками
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Тінь у 4dp
    ) {
        // 4. Column: Розміщуємо елементи вертикально (Картинка -> Текст)
        Column {

            // 5. Box: Контейнер для накладання елементів один на одного.
            // Нам це потрібно, щоб намалювати Сердечко ПОВЕРХ Картинки.
            Box {
                // --- КАРТИНКА ---
                // AsyncImage (з бібліотеки Coil): Завантажує фото з інтернету.
                AsyncImage(
                    model = meal.imageUrl, // URL картинки
                    contentDescription = meal.name, // Опис для незрячих (TalkBack)
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // Фіксована висота картинки
                    contentScale = ContentScale.Crop // Обрізати фото, щоб заповнити весь простір (як cover в CSS)
                )

                // --- ❤️ КНОПКА СЕРДЕЧКА ---
                IconButton(
                    onClick = onToggleFavorite, // При кліку викликаємо функцію, яку нам передали зверху
                    modifier = Modifier
                        .align(Alignment.TopEnd) // Притиснути до правого верхнього кута
                        .padding(8.dp)
                ) {
                    // Логіка іконки:
                    // Якщо isFavorite == true -> Малюємо заповнене серце (Icons.Filled.Favorite)
                    // Якщо isFavorite == false -> Малюємо контур (Icons.Filled.FavoriteBorder)
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        // Логіка кольору: Червоне, якщо улюблене. Біле, якщо ні (щоб було видно на фоні їжі).
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            // --- ТЕКСТОВА ЧАСТИНА ---
            Column(modifier = Modifier.padding(16.dp)) {
                // Назва страви
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.headlineMedium // Великий жирний шрифт
                )
                // Категорія
                Text(
                    // Елвіс-оператор (?:): Якщо категорії немає (null), пишемо "Невідомо"
                    text = "Категорія: ${meal.category ?: "Невідомо"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary // Сірий колір тексту
                )
                Spacer(modifier = Modifier.height(8.dp)) // Відступ

                // Інструкція (короткий опис)
                Text(
                    text = meal.instructions ?: "",
                    maxLines = 3, // Показуємо максимум 3 рядки тексту, щоб картка не була гігантською
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}