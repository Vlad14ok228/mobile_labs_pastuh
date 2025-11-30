package com.lab3.ui.screens.placeDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// Імпорти
import com.lab3.ui.navigation.PlaceListDetailsRoute
import com.lab3.data.placesList

@Composable
fun PlaceDetailsScreen(
    route: PlaceListDetailsRoute, // Сюди прилітає ID
    modifier: Modifier = Modifier
) {
    // Магія: знаходимо потрібне місто за ID
    val placeId = route.id
    val place = placesList.find { it.id == placeId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (place != null) {
            // Малюємо деталі
            Icon(
                imageVector = place.icon,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = place.name,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        } else {
            Text("Помилка: Місце не знайдено")
        }
    }
}