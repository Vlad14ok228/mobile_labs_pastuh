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


@Serializable
data object EntryScreenRoute : NavKey

@Serializable
data class GameScreenRoute(val topic: String) : NavKey

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(EntryScreenRoute)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<EntryScreenRoute> {
                EntryScreen { topic -> backStack.add(GameScreenRoute(topic)) }
            }
            entry<GameScreenRoute> { route ->
                GameScreen(topic = route.topic)
            }
        }
    )
}