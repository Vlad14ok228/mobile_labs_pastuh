package com.vovan.lab7.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.vovan.lab7.ui.screens.entry.EntryScreen
import com.vovan.lab7.ui.screens.subjectDetails.GameScreen
import kotlinx.serialization.Serializable


// --- АДРЕСИ ЕКРАНІВ (ROUTES) ---
// Ці об'єкти використовуються як унікальні ідентифікатори (ключі) для навігатора.

@Serializable
// Адреса для Вітального Екрана (де користувач вводить тему).
data object EntryScreenRoute : NavKey

@Serializable
// Адреса для Екрана Гри. Вона ОБОВ'ЯЗКОВО повинна приймати аргумент.
data class GameScreenRoute(val topic: String) : NavKey
// ☝️ Це ключовий момент: ми передаємо назву теми (topic) на наступний екран,
// щоб GameScreenViewModel знав, які питання генерувати ШІ.

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
) {
    // 1. Ініціалізація Стека Навігації (Історії)
    val backStack = rememberNavBackStack(EntryScreenRoute)
    // ☝️ Встановлює EntryScreenRoute як ПЕРШИЙ (стартовий) екран.

    // 2. NavDisplay: Головний Контейнер UI
    NavDisplay(
        modifier = modifier,
        backStack = backStack, // Вказуємо, за яким стеком треба стежити.
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        // ☝️ entryDecorators: Технічні налаштування для збереження стану та переходів.

        // 3. entryProvider: Карта Маршрутів
        entryProvider = entryProvider {

            // --- МАРШРУТ 1: ВІТАЛЬНИЙ ЕКРАН (EntryScreen) ---
            entry<EntryScreenRoute> {
                EntryScreen { topic ->
                    // ☝️ Це функція-callback, яку викликає EntryScreen, коли користувач натискає "Почати".
                    // Вона передає нам введену користувачем 'topic'.

                    backStack.add(GameScreenRoute(topic))
                    // 👈 Команда 'backStack.add': додає нову адресу у стек.
                    // ВАЖЛИВО: Ми створюємо GameScreenRoute, вкладаючи в неї змінну 'topic'.
                }
            }

            // --- МАРШРУТ 2: ЕКРАН ГРИ (GameScreen) ---
            entry<GameScreenRoute> { route ->
                // ☝️ Навігатор автоматично надає нам об'єкт 'route' (GameScreenRoute).
                GameScreen(topic = route.topic)
                // 👈 Ми витягуємо передану раніше тему (route.topic) і передаємо її на GameScreen.
                // GameScreenViewModel використає цю тему для генерації запиту до ШІ.
            }
        }
    )
}