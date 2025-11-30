@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.lab3.ui.navigation

// --- БЛОК ІМПОРТІВ (Це бібліотеки, які потрібні для роботи навігації) ---
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
// Імпорти ваших екранів (список і деталі)
import com.lab3.ui.screens.placeDetails.PlaceDetailsScreen
import com.lab3.ui.screens.placesList.PlacesListScreen
import kotlinx.serialization.Serializable

// --- АДРЕСИ ЕКРАНІВ (ROUTES) ---
// Це як посилання в інтернеті. Кожен екран має свою унікальну "адресу".

@Serializable
data object PlaceListRoute : NavKey // Адреса для Головного екрану (Списку)

@Serializable
data class PlaceListDetailsRoute(val id: Int) : NavKey // Адреса для Екрану Деталей (приймає ID)

/**
 * NavigationGraph - це головна функція-диспетчер.
 * Вона не малює дизайн, а вирішує, ЯКИЙ екран показати користувачеві.
 */
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
) {
    /**
     * backStack - це "Історія переглядів" (стопка екранів).
     * Ми кажемо: "Почни історію з екрану PlaceListRoute (Список)".
     */
    val backStack = rememberNavBackStack(PlaceListRoute)

    /**
     * NavDisplay - це контейнер ("телевізор"), який показує актуальний екран.
     * Він слідкує за backStack: що там зверху, те й показує.
     */
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        // entryDecorators - це налаштування анімацій і збереження стану (технічний код)
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        // entryProvider - це "Карта маршрутів". Тут ми кажемо: "Якщо адреса така -> покажи цей екран".
        entryProvider = entryProvider {

            // --- МАРШРУТ 1: ГОЛОВНИЙ СПИСОК ---
            // Якщо адреса PlaceListRoute -> показуємо PlacesListScreen
            entry<PlaceListRoute> {
                PlacesListScreen(
                    // Тут ми обробляємо клік по елементу списку.
                    // onDetailsScreen спрацює, коли юзер натисне на місто.
                    onDetailsScreen = { id ->
                        // Ми додаємо в історію (backStack) нову адресу - Екран Деталей з конкретним ID.
                        // NavDisplay це побачить і перемкне екран.
                        backStack.add(PlaceListDetailsRoute(id))
                    }
                )
            }

            // --- МАРШРУТ 2: ДЕТАЛІ ---
            // Якщо адреса PlaceListDetailsRoute -> показуємо PlaceDetailsScreen
            entry<PlaceListDetailsRoute> { route ->
                // 'route' містить ID міста, який ми передали при кліку.
                // Ми передаємо цей 'route' в екран, щоб він знав, що саме малювати.
                PlaceDetailsScreen(route)
            }
        }
    )
}