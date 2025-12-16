// 1. ПАКЕТ: Папка ui/screens
package com.example.smartrecipeapp.ui.screens

// 2. ІМПОРТИ: Нам потрібні класи для UI (Compose) та для системних сповіщень (Android)
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import com.example.smartrecipeapp.R

// 3. EКРАН НАЛАШТУВАНЬ:
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    // 4. ПІДПИСКА НА ДАНІ (StateFlow):
    // Екран "слухає" зміни у ViewModel.
    // Як тільки ти натиснеш перемикач, ViewModel оновить змінну, і екран перемалюється (Recomposition).
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val areNotificationsEnabled by viewModel.areNotificationsEnabled.collectAsState()

    // 5. CONTEXT (Контекст):
    // У Compose немає прямого доступу до системних служб (як NotificationManager).
    // LocalContext.current дає нам місток до системи Android.
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Налаштування",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        SettingsSectionTitle("Загальні")

        // 6. ПЕРЕМИКАЧ ТЕМИ:
        SettingsSwitchItem(
            icon = Icons.Default.Person,
            title = "Темна тема",
            subtitle = "Змінити оформлення додатку",
            checked = isDarkTheme, // Стан беремо з VM
            onCheckedChange = { viewModel.toggleTheme(it) } // Подію відправляємо в VM
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 7. ПЕРЕМИКАЧ СПОВІЩЕНЬ:
        SettingsSwitchItem(
            icon = Icons.Default.Notifications,
            title = "Сповіщення",
            subtitle = "Отримувати нові рецепти",
            checked = areNotificationsEnabled,
            onCheckedChange = { viewModel.toggleNotifications(it) }
        )

        // 8. УМОВНЕ ВІДОБРАЖЕННЯ (Conditional UI):
        // Кнопка тесту з'являється ТІЛЬКИ якщо сповіщення увімкнені.
        if (areNotificationsEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                // При кліку викликаємо нашу функцію створення сповіщення
                onClick = { showTestNotification(context) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Надіслати тестове сповіщення")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSectionTitle("Про додаток")

        // Інформаційна картка (Статичний контент)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Smart Recipe App",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Версія 1.0.0 (Lab 8)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Розроблено студентом політеху 🎓",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// 9. ФУНКЦІЯ СТВОРЕННЯ СПОВІЩЕННЯ:
// Вона не @Composable, це звичайна Kotlin-функція.
// Приймає Context, щоб мати доступ до NotificationManager.
fun showTestNotification(context: Context) {
    val channelId = "recipe_channel"
    val notificationId = 1 // ID сповіщення (якщо відправити з тим самим ID, воно оновиться)

    // Отримуємо системну службу керування сповіщеннями
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // 10. NOTIFICATION CHANNEL (Канал сповіщень):
    // Починаючи з Android 8.0 (Oreo), всі сповіщення мають бути прив'язані до каналу.
    // Це дозволяє користувачу в налаштуваннях телефону вимкнути тільки "Нові рецепти", але залишити "Безпеку".
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Нові рецепти"
        val descriptionText = "Сповіщення про смачні страви"
        val importance = NotificationManager.IMPORTANCE_HIGH // Високий пріоритет (звук + вібрація)
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Реєструємо канал у системі
        notificationManager.createNotificationChannel(channel)
    }

    // 11. BUILDER (Будівельник):
    // Створюємо зовнішній вигляд сповіщення.
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_menu_camera) // Маленька іконка в статус-барі
        .setContentTitle("🍲 Новий рецепт!") // Заголовок
        .setContentText("Шеф підібрав для вас щось смачненьке. Зайдіть переглянути!") // Текст
        .setPriority(NotificationCompat.PRIORITY_HIGH) // Пріоритет для старих Android (< 8.0)
        .setAutoCancel(true) // Сповіщення зникне, коли на нього натиснуть

    // 12. ВІДПРАВКА:
    try {
        notificationManager.notify(notificationId, builder.build())
    } catch (e: SecurityException) {
        // На Android 13+ треба динамічно просити дозвіл POST_NOTIFICATIONS.
        // Тут ми просто ловимо помилку, щоб програма не впала.
    }
}

// Допоміжні компоненти (щоб не дублювати код)
@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            // Switch (Перемикач)
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}