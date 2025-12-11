package com.lab6.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.lab6.ui.screens.current.WeatherScreen
import com.lab6.ui.screens.forecast.WeatherForecastScreen
import com.lab6.ui.screens.menu.MenuScreen
import kotlinx.serialization.Serializable

// --- АДРЕСИ ЕКРАНІВ (ROUTES) ---
// Ці об'єкти використовуються як унікальні ідентифікатори (ключі) для кожного екрана.

@Serializable
// Адреса для екрана Головного Меню.
data object MenuRoute : NavKey

@Serializable
// Адреса для екрана Поточної Погоди.
data object WeatherRoute : NavKey

@Serializable
// Адреса для екрана Прогнозу Погоди.
data object ForecastRoute : NavKey

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
) {
    // 1. Ініціалізація Стека Навігації (Історії)
    val backStack = rememberNavBackStack(MenuRoute)
    // ☝️ rememberNavBackStack створює і керує стеком екранів.
    // MenuRoute вказаний як початковий аргумент — це означає, що MenuScreen буде ПЕРШИМ екраном при запуску.

    // 2. NavDisplay: Головний Контейнер UI
    NavDisplay(
        modifier = modifier,
        backStack = backStack, // Вказуємо, за яким стеком треба стежити.
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        // ☝️ entryDecorators: Технічні налаштування для збереження стану екранів та анімацій переходів.

        // 3. entryProvider: Карта Маршрутів
        entryProvider = entryProvider {

            // --- МАРШРУТ 1: ГОЛОВНЕ МЕНЮ ---
            entry<MenuRoute> {
                // Реєструємо MenuScreen для адреси MenuRoute.
                MenuScreen(
                    // Логіка переходу (Callback-функції, які викликаються при натисканні кнопок у MenuScreen):
                    onWeather = { backStack.add(WeatherRoute) },
                    // ☝️ Команда 'backStack.add': додає адресу WeatherRoute у стек, викликаючи перехід на екран поточної погоди.
                    onWeatherForecast = { backStack.add(ForecastRoute) },
                    // ☝️ Додає адресу ForecastRoute у стек, викликаючи перехід на екран прогнозу.
                )
            }

            // --- МАРШРУТ 2: ПОТОЧНА ПОГОДА ---
            entry<WeatherRoute> {
                // Реєструємо WeatherScreen для адреси WeatherRoute.
                WeatherScreen()
            }

            // --- МАРШРУТ 3: ПРОГНОЗ ПОГОДИ ---
            entry<ForecastRoute> {
                // Реєструємо WeatherForecastScreen для адреси ForecastRoute.
                WeatherForecastScreen()
            }
        }
    )
}