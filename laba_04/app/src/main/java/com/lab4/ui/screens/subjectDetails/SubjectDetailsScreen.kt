package com.lab4.ui.screens.subjectDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab4.data.db.DatabaseStorage
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity
import com.lab4.ui.navigation.SubjectDetailsRoute
import kotlinx.coroutines.launch

@Composable
fun SubjectDetailsScreen(route: SubjectDetailsRoute) {
    val context = LocalContext.current
    val db = DatabaseStorage.getDatabase(context)
    val scope = rememberCoroutineScope()

    var subject by remember { mutableStateOf<SubjectEntity?>(null) }
    var labs by remember { mutableStateOf<List<SubjectLabEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        subject = db.subjectsDao.getSubjectById(route.id)
        labs = db.subjectLabsDao.getSubjectLabsBySubjectId(route.id)
    }

    Column(Modifier.padding(16.dp)) {
        Text(text = subject?.title ?: "", fontSize = 28.sp)
        Spacer(Modifier.height(16.dp))
        Text("Лабораторні роботи:", fontSize = 22.sp)

        LazyColumn {
            items(labs) { lab ->
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
                        Text("Статус: ${lab.status}")
                        Button(onClick = { expanded = !expanded }) {
                            Text("Змінити статус")
                        }

                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("Не розпочато", "В процесі", "Відкладено", "Виконано").forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status) },
                                    onClick = {
                                        expanded = false
                                        scope.launch {
                                            db.subjectLabsDao.updateStatus(lab.id!!, status)
                                            labs = db.subjectLabsDao.getSubjectLabsBySubjectId(route.id)
                                        }
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
                            onClick = {
                                scope.launch {
                                    db.subjectLabsDao.updateComment(lab.id!!, comment)
                                    labs = db.subjectLabsDao.getSubjectLabsBySubjectId(route.id)
                                }
                            },
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
