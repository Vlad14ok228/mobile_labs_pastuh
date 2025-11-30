package com.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lab3.ui.navigation.NavigationGraph
import com.lab3.ui.screens.placeDetails.PlaceDetailsScreen
import com.lab3.ui.screens.placesList.PlacesListScreen
import com.lab3.ui.theme.Lab3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab3Theme {
                // Invocation of navigation container compose function
                NavigationGraph()
            }
        }
    }
}
