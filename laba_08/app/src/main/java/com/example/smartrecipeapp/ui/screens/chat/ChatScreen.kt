// 1. ПАКЕТ: Папка ui/screens/chat
package com.example.smartrecipeapp.ui.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartrecipeapp.data.model.ChatMessage
// Імпорт для отримання ViewModel через Koin
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen() {
    // 2. ОТРИМАННЯ VIEWMODEL:
    // Ми не створюємо "new ChatViewModel()".
    // koinViewModel() каже бібліотеці Koin: "Дай мені готовий мозок для цього екрану".
    val viewModel: ChatViewModel = koinViewModel()

    // 3. ПІДПИСКА НА СТАН (State):
    // collectAsState() перетворює потік даних (Flow) у звичайну змінну.
    // Як тільки ViewModel змінить список повідомлень, цей екран автоматично перемалюється.
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // 4. ЛОКАЛЬНИЙ СТАН:
    // Ця змінна живе тільки тут. Це текст, який користувач зараз вводить у поле.
    // Його не треба зберігати в базі даних.
    var inputText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        // 5. СПИСОК ПОВІДОМЛЕНЬ (LazyColumn):
        // Це аналог RecyclerView. "Lazy" означає, що він малює тільки ті елементи, які видно на екрані.
        // Це економить пам'ять, якщо повідомлень будуть тисячі.
        LazyColumn(
            modifier = Modifier
                .weight(1f) // Займає весь доступний простір, штовхаючи поле вводу вниз
                .padding(16.dp),
            reverseLayout = true // ВАЖЛИВО: Перевертає список. Нові повідомлення з'являються знизу, як у Telegram.
        ) {
            // items(messages.reversed()): Передаємо список.
            // .reversed() потрібен, бо ми перевернули сам лейаут (reverseLayout = true).
            items(messages.reversed()) { msg ->
                MessageBubble(msg) // Малюємо одну "бульбашку"
                Spacer(modifier = Modifier.height(8.dp)) // Відступ між бульбашками
            }
        }

        // 6. ІНДИКАТОР ЗАВАНТАЖЕННЯ:
        // З'являється смужка під списком, тільки коли isLoading == true (бот думає).
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        // 7. ЗОНА ВВОДУ (Текстове поле + Кнопка):
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Вирівнюємо по центру по вертикалі
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it }, // Оновлюємо змінну при кожному натисканні клавіші
                placeholder = { Text("Знайди рецепт з...") },
                modifier = Modifier.weight(1f), // Поле займає всю ширину мінус кнопка
                maxLines = 3
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Кнопка відправки
            IconButton(
                onClick = {
                    viewModel.sendMessage(inputText) // Відправляємо текст у "мозок"
                    inputText = "" // Очищуємо поле вводу
                },
                // Кнопка активна, тільки якщо є текст І бот зараз не зайнятий
                enabled = inputText.isNotBlank() && !isLoading
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

// 8. ОКРЕМИЙ КОМПОНЕНТ "БУЛЬБАШКА":
// Це гарна практика - виносити повторювані елементи в окрему функцію.
@Composable
fun MessageBubble(msg: ChatMessage) {
    // Вибираємо колір залежно від того, хто пише (Я чи Бот)
    val backgroundColor = if (msg.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (msg.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    // Вибираємо сторону: Я -> Справа (CenterEnd), Бот -> Зліва (CenterStart)
    val alignment = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment // Застосовуємо вирівнювання
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Text(
                text = msg.text,
                modifier = Modifier.padding(12.dp),
                color = textColor
            )
        }
    }
}