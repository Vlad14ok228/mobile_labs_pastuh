package com.vovan.lab7.ui.screens.subjectDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(
    topic: String,
    viewModel: GameScreenViewModel = koinViewModel(),
) {
    val isLoading = viewModel.isLoading.collectAsState()
    val quizList = viewModel.quizList.collectAsState()

    LaunchedEffect(topic) {
        viewModel.loadQuiz(topic)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading.value -> CircularProgressIndicator()
                quizList.value != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(quizList.value!!) { item ->
                            var showAnswer by remember { mutableStateOf(false) }
                            Card(onClick = { showAnswer = !showAnswer }) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(item.question, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    AnimatedVisibility(showAnswer) {
                                        Text(item.answer, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                else -> Text("No quiz generated.")
            }
        }
    }
}
