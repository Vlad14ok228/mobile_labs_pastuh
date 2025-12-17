package com.example.smartrecipeapp.ui.screens

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val areNotificationsEnabled by viewModel.areNotificationsEnabled.collectAsState()
    val context = LocalContext.current

    // 👇 1. НОВЕ: Змінна для контролю діалогу
    var showAuthorDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Налаштування",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        SettingsSectionTitle("Загальні")

        SettingsSwitchItem(
            icon = Icons.Default.Person,
            title = "Темна тема",
            subtitle = "Змінити оформлення додатку",
            checked = isDarkTheme,
            onCheckedChange = { viewModel.toggleTheme(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsSwitchItem(
            icon = Icons.Default.Notifications,
            title = "Сповіщення",
            subtitle = "Отримувати нові рецепти",
            checked = areNotificationsEnabled,
            onCheckedChange = { viewModel.toggleNotifications(it) }
        )

        // Твоя логіка для кнопки сповіщень (залишилась без змін)
        if (areNotificationsEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
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

        // 👇 2. ЗМІНЕНО: Додали onClick = { showAuthorDialog = true }
        Card(
            onClick = { showAuthorDialog = true }, // Відкриваємо діалог
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

    // 👇 3. НОВЕ: Сам код діалогового вікна
    if (showAuthorDialog) {
        AlertDialog(
            onDismissRequest = { showAuthorDialog = false },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            title = { Text(text = "Smart Recipe App") },
            text = {
                Text(
                    text = "Цей додаток розроблено в рамках лабораторної роботи №8 .\n\n" +
                            "Технології: Kotlin, Jetpack Compose, Koin, Room, Retrofit, Gemini AI.\n\n" +
                            "Розробник: Студент групи ІК-42"
                )
            },
            confirmButton = {
                TextButton(onClick = { showAuthorDialog = false }) {
                    Text("Зрозуміло")
                }
            }
        )
    }
}

// 👇 Твої функції залишились без змін

fun showTestNotification(context: Context) {
    val channelId = "recipe_channel"
    val notificationId = 1

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Нові рецепти"
        val descriptionText = "Сповіщення про смачні страви"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_menu_camera)
        .setContentTitle("🍲 Новий рецепт!")
        .setContentText("Шеф підібрав для вас щось смачненьке. Зайдіть переглянути!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    try {
        notificationManager.notify(notificationId, builder.build())
    } catch (e: SecurityException) {
        // Handle exception
    }
}

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
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}