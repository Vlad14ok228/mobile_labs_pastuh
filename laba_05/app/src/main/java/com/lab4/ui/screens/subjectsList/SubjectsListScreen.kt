package com.lab4.ui.screens.subjectsList

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubjectsListScreen(
    viewModel: SubjectsListViewModel = koinViewModel(), // отримуємо ViewModel через Koin
    onDetailsScreen: (Int) -> Unit,
) {
    val subjectsListState = viewModel.subjectListStateFlow.collectAsState()

    LazyColumn(Modifier.fillMaxSize()) {
        items(subjectsListState.value) { subject ->
            Text(
                text = subject.title,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable(
                        interactionSource = null,
                        indication = LocalIndication.current,
                    ) { subject.id?.let { id -> onDetailsScreen(id) } }
            )
        }
    }
}
