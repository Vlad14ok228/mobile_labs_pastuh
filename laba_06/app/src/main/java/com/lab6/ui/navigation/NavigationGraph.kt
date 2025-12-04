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

@Serializable
data object MenuRoute : NavKey
@Serializable
data object WeatherRoute : NavKey
@Serializable
data object ForecastRoute : NavKey

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(MenuRoute)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<MenuRoute> {
                MenuScreen(
                    onWeather = { backStack.add(WeatherRoute) },
                    onWeatherForecast = { backStack.add(ForecastRoute) },
                )
            }
            entry<WeatherRoute> {  WeatherScreen() }
            entry<ForecastRoute> { WeatherForecastScreen() }
        }
    )
}