// 1. ПАКЕТ: Кореневий пакет додатку
package com.example.smartrecipeapp

// 2. ІМПОРТИ
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // 👈 Дозволяє створити ViewModel, яка живе поки живе додаток
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState // 👈 Перетворює потік даних (Flow) на стан UI
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.smartrecipeapp.ui.navigation.Screen
import com.example.smartrecipeapp.ui.screens.*
import com.example.smartrecipeapp.ui.screens.chat.ChatScreen
import com.example.smartrecipeapp.ui.screens.details.DetailsScreen
import com.example.smartrecipeapp.ui.screens.home.HomeScreen
import com.example.smartrecipeapp.ui.theme.SmartRecipeAppTheme

// 3. MAIN ACTIVITY: Головний клас, який запускається при старті додатку.
// Успадковується від ComponentActivity (базовий клас для Jetpack Compose).
class MainActivity : ComponentActivity() {

    // 4. ГЛОБАЛЬНА VIEWMODEL:
    // Ми створюємо SettingsViewModel тут, на найвищому рівні.
    // Чому? Бо налаштування (тема) впливають на ВЕСЬ додаток, а не на один екран.
    // by viewModels() гарантує, що об'єкт створиться один раз і житиме, навіть якщо перевернути телефон.
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent — це місце, де починається малювання інтерфейсу (Compose).
        setContent {

            // 5. СЛУХАЧ ТЕМИ:
            // Ми підписуємося на змінну isDarkTheme з ViewModel.
            // Як тільки користувач клацне перемикач у налаштуваннях, ця змінна зміниться,
            // і MainActivity миттєво перемалює весь інтерфейс.
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

            // 6. ОБГОРТКА ТЕМИ:
            // Ми передаємо параметр darkTheme. Це "магічний" рядок, який каже додатку,
            // які кольори брати (світлі чи темні).
            SmartRecipeAppTheme(darkTheme = isDarkTheme) {

                // 7. НАВІГАЦІЯ:
                // Створюємо контролер ("водія"), який керує переходами між екранами.
                val navController = rememberNavController()

                // Список екранів для нижнього меню
                val items = listOf(
                    Screen.Home,
                    Screen.Favorites,
                    Screen.Chat,
                    Screen.Settings
                )

                // 8. SCAFFOLD (Риштування):
                // Стандартний макет Android з місцями для TopBar, BottomBar тощо.
                Scaffold(
                    bottomBar = {
                        // Малюємо нижню панель навігації
                        NavigationBar {
                            // Отримуємо поточний маршрут, щоб підсвітити активну іконку
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon!!, contentDescription = null) },
                                    label = { Text(screen.title) },
                                    // Перевірка: чи ми зараз на цьому екрані? (selected = true/false)
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        // Логіка переходу при кліку на іконку меню
                                        navController.navigate(screen.route) {
                                            // popUpTo: Очищаємо історію переходів до головного екрану,
                                            // щоб кнопка "Назад" не ганяла нас по колу.
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true // Зберігаємо стан скролу
                                            }
                                            // launchSingleTop: Не відкриваємо екран ще раз, якщо він вже відкритий.
                                            launchSingleTop = true
                                            // restoreState: Відновлюємо стан (наприклад, текст у полі пошуку), якщо повернулися.
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    // 9. NAV HOST (Контейнер контенту):
                    // Саме тут міняються екрани. innerPadding потрібен, щоб контент не ховався за нижнім меню.
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Екран 1: Головна
                        composable(Screen.Home.route) {
                            HomeScreen(
                                // Callback: коли клікнули на страву -> переходимо на деталі
                                onMealClick = { mealId ->
                                    navController.navigate(Screen.Details.createRoute(mealId))
                                }
                            )
                        }

                        // Екран 2: Збережені
                        composable(Screen.Favorites.route) {
                            FavoritesScreen(
                                onMealClick = { mealId ->
                                    navController.navigate(Screen.Details.createRoute(mealId))
                                }
                            )
                        }

                        // Екран 3: Чат
                        composable(Screen.Chat.route) { ChatScreen() }

                        // Екран 4: Налаштування
                        composable(Screen.Settings.route) {
                            // 👇 ВАЖЛИВО: Ми передаємо ту саму ViewModel, яку створили зверху.
                            // SettingsScreen змінює налаштування в цій VM, а MainActivity це бачить і змінює тему.
                            SettingsScreen(viewModel = settingsViewModel)
                        }

                        // Екран 5: Деталі (відкривається окремо, не з меню)
                        composable(Screen.Details.route) { backStackEntry ->
                            // Витягуємо ID страви з аргументів навігації
                            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
                            DetailsScreen(mealId = mealId)
                        }
                    }
                }
            }
        }
    }
}