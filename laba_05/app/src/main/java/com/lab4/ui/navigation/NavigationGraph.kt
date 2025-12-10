package com.lab4.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.lab4.ui.screens.subjectDetails.SubjectDetailsScreen
import com.lab4.ui.screens.subjectsList.SubjectsListScreen
import kotlinx.serialization.Serializable

// --- АДРЕСИ ЕКРАНІВ (ROUTES) ---

@Serializable
// Адреса 1: Головний екран (Список предметів).
// Це просто об'єкт, бо йому не треба передавати ніяких параметрів.
data object SubjectsListRoute : NavKey

@Serializable
// Адреса 2: Екран деталей (Список лаб для конкретного предмета).
// Це data class, бо він ОБОВ'ЯЗКОВО приймає 'id' предмета.
// Завдяки цьому ID ми знатимемо, які саме лаби витягувати з бази даних.
data class SubjectDetailsRoute(val id: Int) : NavKey

@Composable
fun NavigationGraph(modifier: Modifier = Modifier) {

    // Ініціалізація стека навігації (історії переглядів).
    // Ми вказуємо, що стартовим екраном є SubjectsListRoute.
    val backStack = rememberNavBackStack(SubjectsListRoute)

    // NavDisplay - це контейнер, який "дивиться" на backStack і малює верхній екран.
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        // entryProvider - це "карта", яка зв'язує Адресу -> з Екраном.
        entryProvider = entryProvider {

            // --- МАРШРУТ 1: СПИСОК ПРЕДМЕТІВ ---
            entry<SubjectsListRoute> {
                SubjectsListScreen(
                    // Логіка переходу: Що робити, коли клікнули на предмет?
                    onDetailsScreen = { id ->
                        // Ми додаємо в стек нову адресу (Деталі) і вкладаємо туди ID натиснутого предмета.
                        // Навігатор бачить зміни в стеку і відкриває новий екран.
                        backStack.add(SubjectDetailsRoute(id))
                    }
                )
            }

            // --- МАРШРУТ 2: ДЕТАЛІ ПРЕДМЕТА ---
            entry<SubjectDetailsRoute> { route ->
                // 'route' - це об'єкт SubjectDetailsRoute, який містить 'id'.
                // Ми передаємо цей 'route' в екран, щоб SubjectDetailsScreen міг
                // звернутися до бази даних і сказати: "Дай мені лаби для предмета з цим ID".
                SubjectDetailsScreen(route)
            }
        }
    )
}