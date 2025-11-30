package com.lab3.data // <-- Зверни увагу, пакет тепер "data"

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

// Модель даних
data class Place(
    val id: Int,
    val name: String,
    val description: String,
    val icon: ImageVector
)

// Список міст
val placesList = listOf(
    Place(
        id = 1,
        name = "Київ",
        description = "Столиця України. Хрещатик, Софія Київська та смачна їжа.",
        icon = Icons.Default.Home
    ),
    Place(
        id = 2,
        name = "Львів",
        description = "Місто кави, левів та старовинної архітектури.",
        icon = Icons.Default.Star
    ),
    Place(
        id = 3,
        name = "Одеса",
        description = "Море, гумор, Потьомкінські сходи та Оперний театр.",
        icon = Icons.Default.LocationOn
    ),
    Place(
        id = 4,
        name = "Карпати",
        description = "Гори, чисте повітря і Говерла.",
        icon = Icons.Default.LocationOn
    ),
    Place(
        id = 5,
        name = "Крим",
        description = "Море, відпочинок, гарні краєвиди та смачна їжа !.",
        icon = Icons.Default.LocationOn
    )
)