package com.vovan.lab7.ui.screens.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntryScreen(onStartGame: (String) -> Unit) {
    val topics = listOf("History", "Science", "Sport")
    var customTopic by remember { mutableStateOf("") }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Text("Choose a quiz topic:")

            topics.forEach { topic ->
                Card(
                    onClick = { onStartGame(topic) },
                    modifier = Modifier.padding(8.dp).height(60.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(topic)
                    }
                }
            }

            Text("Or enter your own topic:")
            TextField(
                value = customTopic,
                onValueChange = { customTopic = it },
                placeholder = { Text("Enter topic...") }
            )
            Button(onClick = { if (customTopic.isNotBlank()) onStartGame(customTopic) }) {
                Text("Start Quiz")
            }
        }
    }
}


