package com.lab4.ui.screens.subjectDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab4.ui.navigation.SubjectDetailsRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubjectDetailsScreen(
    route: SubjectDetailsRoute,
    viewModel: SubjectDetailsViewModel = koinViewModel()
) {
    val subjectState = viewModel.subjectStateFlow.collectAsState()
    val labsState = viewModel.subjectLabsListStateFlow.collectAsState()

    LaunchedEffect(Unit) { viewModel.initData(route.id) }

    Column(Modifier.padding(16.dp)) {
        Text(subjectState.value?.title ?: "", fontSize = 28.sp)
        Spacer(Modifier.height(16.dp))
        Text("Лабораторні роботи:", fontSize = 22.sp)

        LazyColumn {
            items(labsState.value) { lab ->
                var expanded by remember { mutableStateOf(false) }
                var comment by remember { mutableStateOf(lab.comment ?: "") }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(text = lab.title, fontSize = 20.sp)
                        Text(text = lab.description, fontSize = 16.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("Статус: ${lab.status ?: "Невідомо"}")

                        Button(onClick = { expanded = !expanded }) {
                            Text("Змінити статус")
                        }

                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("Не розпочато", "В процесі", "Відкладено", "Виконано").forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status) },
                                    onClick = {
                                        expanded = false
                                        viewModel.updateStatus(lab.id!!, status)
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        Text("Коментар:")
                        BasicTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        )
                        Button(
                            onClick = { viewModel.updateComment(lab.id!!, comment) },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Зберегти коментар")
                        }
                    }
                }
            }
        }
    }
}
